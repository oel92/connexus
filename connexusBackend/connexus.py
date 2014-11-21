from google.appengine.ext import blobstore, deferred
from google.appengine.ext.webapp import blobstore_handlers
from google.appengine.api import users
from google.appengine.ext import ndb
from google.appengine.api import images

from google.appengine.api import mail

from google.appengine.api import files, images
from google.appengine.ext.blobstore import BlobKey


import time
import threading
import datetime
import webapp2
import cgi
import jinja2
import os
import urllib
import re
from random import uniform

from urlparse import urlparse, parse_qs

import json



WEBSITE = 'https://blueimp.github.io/jQuery-File-Upload/'
MIN_FILE_SIZE = 1  # bytes
MAX_FILE_SIZE = 5000000  # bytes
IMAGE_TYPES = re.compile('image/(gif|p?jpeg|(x-)?png)')
ACCEPT_FILE_TYPES = IMAGE_TYPES
THUMBNAIL_MODIFICATOR = '=s80'  # max width / height
EXPIRATION_TIME = 300  # seconds


def cleanup(blob_keys):
    blobstore.delete(blob_keys)




JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)



DEFAULT_STREAM_NAME = 'default_stream'
FIVE_IN_HOUR = 60/5
FIVE_IN_DAY = 24*60/5


# We set a parent key on the 'Streams' to ensure that they are all in the same
# entity group. Queries across the single entity group will be consistent.
# However, the write rate should be limited to ~1/second.

def stream_key(stream_name=DEFAULT_STREAM_NAME):
    """Constructs a Datastore key for a Stream entity with stream_name."""
    return ndb.Key('Stream', stream_name)
	
	
class Stream(ndb.Model):
	"""Models an individual Stream entry."""
	name = ndb.StringProperty()							#name of stream
	owner = ndb.StringProperty()							#owner of stream
	
	date_added = ndb.DateTimeProperty(auto_now_add=True)				#date added
	date_updated = ndb.DateTimeProperty(auto_now_add=True)			#date of last new picture
	date_only_updated = ndb.DateProperty(auto_now_add=True)
	datetime_viewed = ndb.DateTimeProperty(repeated = True)
	last_hour_views = ndb.IntegerProperty(default=True)
	
	viewcount = ndb.IntegerProperty(required=True)					#number of views on view a single stream page
	num_pics = ndb.IntegerProperty(required=True)					#number of images in stream
	cover = ndb.StringProperty(indexed=False)			#url of the cover image
	
	tags = ndb.StringProperty(repeated = True)			#list of tags associated with this stream
	subscribers = ndb.StringProperty(repeated = True) 	#list of users subscribed

	
class Image(ndb.Model):
	"""Models an individual Image entry"""
	date = ndb.DateTimeProperty(auto_now_add=True)
	blob_url = ndb.StringProperty(indexed=False)
	blob_key = ndb.BlobKeyProperty()
	comment = ndb.StringProperty(indexed=False, default=True)
	latitude = ndb.FloatProperty()
	longitude = ndb.FloatProperty()
	
class User(ndb.Model):
	"""Models a User's email rate properties"""
	email = ndb.StringProperty()
	zero = ndb.StringProperty()
	five = ndb.StringProperty()
	hour = ndb.StringProperty()
	day = ndb.StringProperty()

class Time_Counter:
	def __init__(self):
		self.hour_count = 0
		self.day_count = 0	
		
	def get_hour_count(self):
		return self.hour_count
		
	def get_day_count(self):
		return self.day_count
		
	def inc_count(self):
		self.hour_count = (self.hour_count + 1) % FIVE_IN_HOUR
		self.day_count = (self.day_count + 1) % FIVE_IN_DAY
		
counter = Time_Counter()

class FrontPage(webapp2.RequestHandler):
	def get(self):
		if (not(users.get_current_user())):
			url = users.create_login_url(self.request.uri)
			url_linktext = 'Login'
			template_values = {'url': url, 'url_linktext' : url_linktext}	
			template = JINJA_ENVIRONMENT.get_template('Login.html')
			self.response.write(template.render(template_values))
		else:
			self.redirect('/manage')
			
class StreamView(webapp2.RequestHandler):
	def get(self):
		#get stream key from page
		post_upload = self.request.get("upload")		#tells whether page was loaded because of an upload
		more_pictures = self.request.get("moreButton")		#tells whether page loaded because of more pictures
		subscribe_note = self.request.get("subscribe_msg")
		urlString = self.request.get("stream_key")
				

		streamKey = ndb.Key(urlsafe=urlString)
		stream_obj = streamKey.get()
		
		if not post_upload:
			stream_obj.viewcount = stream_obj.viewcount + 1
			stream_obj.datetime_viewed.append(datetime.datetime.now())
			stream_obj.put()
		
		images_query = Image.query(ancestor=streamKey).order(-Image.date)
		images = images_query.fetch(10)
		
		upload_url = blobstore.create_upload_url('/upload')
	
		#For Login/Logout url
		user = users.get_current_user()
		if user:
			url = users.create_logout_url(self.request.uri)
			url_linktext = 'Logout'
		else:
			url = users.create_login_url(self.request.uri)
			url_linktext = 'Login'
			
		#If user just logged in don't display message when page is reloaded
		if (user and subscribe_note != ""):
			subscribe_note = ""

			
		# Write the template values and render template
		template_values = {'images': images, 'upload': upload_url, 'stream_key': cgi.escape(stream_obj.key.urlsafe()), 'stream_name': cgi.escape(stream_obj.name), 'owner': cgi.escape(stream_obj.owner), 'subscribe_note': subscribe_note, 'url': url, 'url_linktext': url_linktext, 'more_pictures' : more_pictures}
		template= JINJA_ENVIRONMENT.get_template('singleview.html')
		self.response.write(template.render(template_values))
		
	def post(self):
		user = users.get_current_user()
		
		urlString = self.request.get("stream_key")
		streamKey = ndb.Key(urlsafe=urlString)
		stream_obj = streamKey.get()
		
		if user:
			stream_obj.subscribers.append(user.email())
			stream_obj.put()

			query_params = {'stream_key': urlString, "subscribe_msg": ""}
			self.redirect('/streamview?' + urllib.urlencode(query_params))
		else:
			query_params = {'stream_key': urlString, "subscribe_msg": "Please login to subscribe to a stream"}
			self.redirect('/streamview?' + urllib.urlencode(query_params))


class UploadHandler(webapp2.RequestHandler):

    def initialize(self, request, response):
        super(UploadHandler, self).initialize(request, response)
        self.response.headers['Access-Control-Allow-Origin'] = '*'
        self.response.headers[
            'Access-Control-Allow-Methods'
        ] = 'OPTIONS, HEAD, GET, POST, PUT, DELETE'
        self.response.headers[
            'Access-Control-Allow-Headers'
        ] = 'Content-Type, Content-Range, Content-Disposition'

    def validate(self, file):
        if file['size'] < MIN_FILE_SIZE:
            file['error'] = 'File is too small'
        elif file['size'] > MAX_FILE_SIZE:
            file['error'] = 'File is too big'
        elif not ACCEPT_FILE_TYPES.match(file['type']):
            file['error'] = 'Filetype not allowed'
        else:
            return True
        return False

    def get_file_size(self, file):
        file.seek(0, 2)  # Seek to the end of the file
        size = file.tell()  # Get the position of EOF
        file.seek(0)  # Reset the file position to the beginning
        return size

    def write_blob(self, data, info):
        blob = files.blobstore.create(
            mime_type=info['type'],
            _blobinfo_uploaded_filename=info['name']
        )
        with files.open(blob, 'a') as f:
            f.write(data)
        files.finalize(blob)
        return files.blobstore.get_blob_key(blob)

    def handle_upload(self):
	print "test"
        results = []
        blob_keys = []
        for name, fieldStorage in self.request.POST.items():
            if type(fieldStorage) is unicode:
                continue
            result = {}
            result['name'] = re.sub(
                r'^.*\\',
                '',
                fieldStorage.filename
            )
            result['type'] = fieldStorage.type
            result['size'] = self.get_file_size(fieldStorage.file)

		
            if self.validate(result):
                blob_key = str(
                    self.write_blob(fieldStorage.value, result)
                )
                blob_keys.append(blob_key)
                result['deleteType'] = 'DELETE'
                result['deleteUrl'] = self.request.host_url +\
                    '/?key=' + urllib.quote(blob_key, '')
		

		#image = Image(parent=streamKey)
			
		#upload_files = self.get_uploads('file')
		#if (len(upload_files) > 0 ):
		#	blob_info = upload_files[0]
		#	website = images.get_serving_url(blob_info)
		#	image.blob_url = website
		#	image.blob_key = blob_info.key()
		#	image.comment = self.request.get("comments")
		#	image.put()
		#	stream_obj.date_updated = datetime.datetime.now()
		#	stream_obj.date_only_updated = datetime.datetime.now().date()
		#	stream_obj.num_pics = stream_obj.num_pics + 1
		#	stream_obj.put()
				
									
		#query_params = {'stream_key': stream_obj.key.urlsafe(), 'upload': True}
		#self.redirect('/streamview?' + urllib.urlencode(query_params))


                if (IMAGE_TYPES.match(result['type'])):
                    try:
			print "hello"
			
                        result['url'] = images.get_serving_url(
                            blob_key,
                            secure_url=self.request.host_url.startswith(
                                'https'
                            )
                        )
			print result['url']
                        result['thumbnailUrl'] = result['url'] +\
                            THUMBNAIL_MODIFICATOR
			

			print str(fieldStorage.file)
			print str(fieldStorage)
			print str(fieldStorage.type)
			print blob_key
			
				
			urlString = self.request.get("stream_key")
			streamKey = ndb.Key(urlsafe=urlString)
			image = Image(parent=streamKey)
			
			image.blob_url = result['url']
			key = str(blob_key)
			print key
			image.blob_key = BlobKey(key)
			image.comment = ""	
			long = uniform(-180,180)
			lat = uniform(-70, 70)
			image.longitude = long
			image.latitude = lat
			image.put()
			

			
			stream_obj = streamKey.get()
			stream_obj.date_updated = datetime.datetime.now()
			stream_obj.date_only_updated = datetime.datetime.now().date()
			stream_obj.num_pics = stream_obj.num_pics + 1
			stream_obj.put()
			
                    except:  # Could not get an image serving url
                        pass
                if not 'url' in result:
                    result['url'] = self.request.host_url +\
                        '/' + blob_key + '/' + urllib.quote(
                            result['name'].encode('utf-8'), '')
            results.append(result)
        deferred.defer(
            cleanup,
            blob_keys,
            _countdown=EXPIRATION_TIME
        )
        return results

    def options(self):
        pass

    def head(self):
        pass

    
    def post(self):
	user = users.get_current_user()
		
	if user:
        	if (self.request.get('_method') == 'DELETE'):
        	    return self.delete()
        	result = {'files': self.handle_upload()}
        	s = json.dumps(result, separators=(',', ':'))
        	redirect = self.request.get('redirect')
        	if redirect:
        	    return self.redirect(str(
        	        redirect.replace('%s', urllib.quote(s, ''), 1)
        	    ))
        	if 'application/json' in self.request.headers.get('Accept'):
        	    self.response.headers['Content-Type'] = 'application/json'
        	self.response.write(s)
	else:
		self.redirect('/')

    def delete(self):
        key = self.request.get('key') or ''
        blobstore.delete(key)
        s = json.dumps({key: True}, separators=(',', ':'))
        if 'application/json' in self.request.headers.get('Accept'):
            self.response.headers['Content-Type'] = 'application/json'
        self.response.write(s)


		
class Manage(webapp2.RequestHandler):
	def get(self):
		user = users.get_current_user()
		
		if user:
			temp = self.request.get("num_checked")
			#Gets all streams that match owner field to this user, sorted in decreasing date order
			stream_query = Stream.query(ancestor=stream_key(DEFAULT_STREAM_NAME))
			stream_query = stream_query.filter(Stream.owner == user.nickname())
			stream_query = stream_query.order(-Stream.date_updated)
			owned_streams = stream_query.fetch()
			
			stream_query = Stream.query(ancestor=stream_key(DEFAULT_STREAM_NAME))
			stream_query = stream_query.filter(Stream.subscribers == user.email())
			stream_query = stream_query.order(-Stream.date_updated)
			subscribe_streams = stream_query.fetch()
		
			url = users.create_logout_url(self.request.uri)
			url_linktext = 'Logout'
			
			template_values = {'owned_streams': owned_streams, 'subscribe_streams': subscribe_streams, 'url': url, 'url_linktext': url_linktext}
			template = JINJA_ENVIRONMENT.get_template('manage.html')
			self.response.write(template.render(template_values))
		else:
			self.redirect('/')
						
	def post(self):
		user = users.get_current_user()
		if user:
			request = self.request.get("button")
			
			if (request == "delete_clicked"):
				check_list = self.request.get_all("delete")
				for value in check_list:
					streamKey = ndb.Key(urlsafe=value)
					stream_obj = streamKey.get()
					images_query = Image.query(ancestor=streamKey)
					image_list = images_query.fetch()
					for imagez in image_list:
						blobstore.delete(imagez.blob_key)
						imagez.key.delete()
					stream_obj.key.delete()
			elif (request == "unsub_clicked"):
				check_list = self.request.get_all("unsubscribe")
				for value in check_list:
					streamKey = ndb.Key(urlsafe=value)
					stream_obj = streamKey.get()
					stream_obj.subscribers.remove(user.email())
					stream_obj.put()
			self.redirect('/manage')
		else:
			self.redirect('/')
		
				
class Create(webapp2.RequestHandler):
	def get(self):
		template_values = { }
		template = JINJA_ENVIRONMENT.get_template('create.html')
        	self.response.write(template.render(template_values))
			
	def post(self):	
		user = users.get_current_user()
		if user:
			stream_title = self.request.get('stream_title')
			#check if stream_title was left blank
			if stream_title == "":
				query_params = {'error_msg': "Stream name was left blank! Operation did not complete."}
				self.redirect('/error?' + urllib.urlencode(query_params))				
				return
			#Check if there already exists a stream with the same name
			stream_query = Stream.query(ancestor=stream_key(DEFAULT_STREAM_NAME))
			streams = stream_query.fetch()
			for stream in streams:
				if stream_title == stream.name:
					query_params = {'error_msg': "Stream with same name already exists! Operation did not complete."}
					self.redirect('/error?' + urllib.urlencode(query_params))
					return
			subscribers = self.request.get('subscribers')
			message = self.request.get('message')
			tags = self.request.get('streamtags')
			cover_url = self.request.get('imageURL')
			#TODO check if cover_url is not legit, output an error or some shit
			
			subscribers = re.sub("\s*", "", subscribers)
			subscribers_list = subscribers.split(',')
			#subscribers_list.append(user.nickname())
			
			tags = re.sub("\s*|#", "", tags)
			tags_list = tags.split(',')
			
			new_stream = Stream(parent=stream_key(DEFAULT_STREAM_NAME))
			new_stream.name = stream_title
			new_stream.owner = user.nickname()
			new_stream.subscribers = []
			new_stream.tags = tags_list
			new_stream.cover = cover_url
			new_stream.viewcount = 0
			new_stream.num_pics = 0
			new_stream.last_hour_views = 0
			new_stream.put()

			query_params = {'stream_key' : new_stream.key.urlsafe()}
			link = "connexus-os.appspot.com/streamview?" + urllib.urlencode(query_params);
			for email in subscribers_list:
				if email != "":
					if not message:

						mail.send_mail(sender="Connexus<oliver.tjc@gmail.com>",
              						to=email,
             						subject="Someone has shared a stream with you!",
              						body="""
						Hello,

						""" + user.nickname() + """ has shared a stream with you! Check it out here: """ + link + """

						Connexus Team

			
						""")
					else:
						mail.send_mail(sender="Connexus<oliver.tjc@gmail.com>",
              						to=email,
             						subject="Connexus",
              						body="""
						Hello,

						""" + user.nickname() + """ has shared a stream with you! Check it out here: """ + link + """
						
						Here's a personal message from """ + user.nickname() + """: """ + message + """
						
						Connexus Team

			
						""")
					
			self.redirect('/manage')
		else:
			self.redirect('/')
		

class View(webapp2.RequestHandler):
	def get(self):
		stream_query = Stream.query(ancestor=stream_key(DEFAULT_STREAM_NAME)).order(Stream.date_added)
		streams = stream_query.fetch()
		
		#If no cover_url, sets first (oldest) picture in stream to be cover
		for stream in streams:
			if stream.cover == "" and stream.num_pics != 0:
				query = Image.query(ancestor=stream.key).order(Image.date)
				images = query.fetch(1)
				stream.cover = images[0].blob_url
				stream.put()
		
		#For Login/Logout url
		user = users.get_current_user()
		if user:
			url = users.create_logout_url(self.request.uri)
			url_linktext = 'Logout'
		else:
			url = users.create_login_url(self.request.uri)
			url_linktext = 'Login'
			
		template_values = {"streams": streams, "url": url, "url_linktext": url_linktext}
		template = JINJA_ENVIRONMENT.get_template('view.html')
		self.response.write(template.render(template_values))
	
class Search(webapp2.RequestHandler):
	def get(self):
		click = self.request.get("button")
		if (click == "rebuild"):
			print "hi"

		search_text = self.request.get("search", "")
		search_text.lower()
		search_done = False
		result_streams = []
		
		if search_text != "":
			search_done = True
			stream_query = Stream.query(ancestor=stream_key(DEFAULT_STREAM_NAME)).order(Stream.date_added)
			streams = stream_query.fetch()
			
			for stream in streams:
				if search_text.lower() in stream.name.lower():
					result_streams.append(stream)
					continue
				for tag in stream.tags:
					if search_text.lower() in tag.lower():
						result_streams.append(stream)
						break
			
		template_values = {"result_streams": result_streams, "search_done": search_done, "search_text": search_text}
		template = JINJA_ENVIRONMENT.get_template('search.html')
		self.response.write(template.render(template_values))
		
class Trending(webapp2.RequestHandler):
	def get(self):
		user = users.get_current_user()
		zero = "checked"
		five = ""
		hour = ""
		day = ""
		
		#delete all users for debugging
		# user_query = User.query()
		# user_in_db = user_query.fetch()
		# for userz in user_in_db:
			# userz.key.delete()
			
		#Get top 3 streams
		stream_query = Stream.query(ancestor=stream_key(DEFAULT_STREAM_NAME)).order(-Stream.last_hour_views)
		top_streams = stream_query.fetch(3)
							
		if user:
			user_query = User.query(ancestor = stream_key(DEFAULT_STREAM_NAME))
			user_query = user_query.filter(User.email == user.email())
			user_in_db = user_query.fetch()
			#If user found in database, set radio values to be displayed 
			if user_in_db:
				temp = "User found in database"
				zero = user_in_db[0].zero
				five = user_in_db[0].five
				hour = user_in_db[0].hour
				day = user_in_db[0].day
			#if user not found in database, add the user and set zero = "checked" so radio button is filled
			else:
				temp = "Adding user to database"
				new_user = User(parent=stream_key(DEFAULT_STREAM_NAME))
				new_user.email = user.email()
				new_user.zero = "checked"
				new_user.five = ""
				new_user.hour = ""
				new_user.day = ""
				new_user.put()
				
			#For Login/Logout url
			url = users.create_logout_url(self.request.uri)
			url_linktext = 'Logout'
		else:
			temp = "Nobody logged in "
			url = users.create_login_url(self.request.uri)
			url_linktext = 'Login'
					
		template_values = {"top_streams": top_streams, "zero": zero, "five": five, "hour": hour, "day": day, "url": url, "url_linktext": url_linktext}
		template = JINJA_ENVIRONMENT.get_template('trending.html')
		self.response.write(template.render(template_values))
	
	def post(self):
		user = users.get_current_user()
		
		if user:
			value = self.request.get("rate")
			user_query = User.query(ancestor = stream_key(DEFAULT_STREAM_NAME))
			user_query = user_query.filter(User.email == user.email())
			user_in_db = user_query.fetch()
			
			#If user found in database, set radio values to be displayed 
			if user_in_db:
				if(value == "zero"):
					user_in_db[0].zero = "checked"
					user_in_db[0].five = ""
					user_in_db[0].hour = ""
					user_in_db[0].day = ""
				elif(value == "five"):
					user_in_db[0].zero = ""
					user_in_db[0].five = "checked"
					user_in_db[0].hour = ""
					user_in_db[0].day = ""
				elif(value == "hour"):
					user_in_db[0].zero = ""
					user_in_db[0].five = ""
					user_in_db[0].hour = "checked"
					user_in_db[0].day = ""
				elif(value == "day"):
					user_in_db[0].zero = ""
					user_in_db[0].five = ""
					user_in_db[0].hour = ""
					user_in_db[0].day = "checked"
				user_in_db[0].put()
					
			self.redirect('/trending')
		else:
			self.redirect('/')

class Error(webapp2.RequestHandler):
	def get(self):
		error_msg = self.request.get("error_msg")
		template_values = {"error_msg": error_msg}
		template = JINJA_ENVIRONMENT.get_template('Error.html')
		self.response.write(template.render(template_values))
		
		
class Cron(webapp2.RequestHandler):
	def get(self):
		global counter
		email_addresses = []
		top_streams = []
				
		#Get users by rate 
		user_query = User.query(ancestor = stream_key(DEFAULT_STREAM_NAME))
		
		five_query = user_query.filter(User.five == "checked")
		five_list = five_query.fetch()
		
		hour_query = user_query.filter(User.hour == "checked")
		hour_list = hour_query.fetch()
		
		day_query = user_query.filter(User.day == "checked")
		day_list = day_query.fetch()

		######################       Compile list of email recipients   ####################
		for user in five_list:
			email_addresses.append(user.email)
		
		if(counter.get_hour_count() == 0):
			for user in hour_list:
				email_addresses.append(user.email)
				
		if(counter.get_day_count() == 0):
			for user in day_list:
				email_addresses.append(user.email)
				
				
		#####################    Find top three streams in past hour   ######################
		hour_ago = datetime.datetime.now() - datetime.timedelta(hours = 1)
		
		stream_query = Stream.query(ancestor=stream_key(DEFAULT_STREAM_NAME))
		all_streams = stream_query.fetch()
		for stream in all_streams:
			for view in stream.datetime_viewed[:]:
				if view < hour_ago:
					stream.datetime_viewed.remove(view)
			stream.last_hour_views =  len(stream.datetime_viewed)
			stream.put()
			
		stream_query = Stream.query(ancestor=stream_key(DEFAULT_STREAM_NAME)).order(-Stream.last_hour_views)
		top_streams = stream_query.fetch(3)
		topStreams = ""

		for i in range(len(top_streams)):
			if i == 0:
				topStreams = top_streams[i].name + " (" + str(top_streams[i].last_hour_views) + " views in the last hour)"
			else:
				topStreams = topStreams + ", " + top_streams[i].name + " (" + str(top_streams[i].last_hour_views) + " views in the last hour)"
		
		emails = ", ".join(email_addresses)
		
	
		mail.send_mail(sender="Connexus<oliver.tjc@gmail.com>",
			to=emails, 
			subject="Top Trending Streams",
          		body="""
		Hello,

		Here are the top trending streams: """ + topStreams + """

		Connexus Team

			
		""")
		
		counter.inc_count()
		
class AutoComplete(webapp2.RequestHandler):
	def get(self):	
		
		search_text = self.request.get("search")
		search_text.lower()
			
		result_streams = []
		auto_results = []	

		if search_text != "":
			
			stream_query = Stream.query(ancestor=stream_key(DEFAULT_STREAM_NAME)).order(Stream.date_added)
			streams = stream_query.fetch()
			
			for stream in streams:
				if search_text in stream.name.lower():
					result_streams.append(stream)
					continue
				for tag in stream.tags:
					if search_text in tag.lower():
						result_streams.append(stream)
						break
				
		for result in result_streams:
			auto_results.append(result.name)
			
		self.response.write(json.dumps(sorted(auto_results, key=lambda result: result.lower())))
		
class GeoView(webapp2.RequestHandler):
	def get(self):
		
		template_values = {}
		template = JINJA_ENVIRONMENT.get_template('map.html')
		self.response.write(template.render(template_values))

class GetGeoTags(webapp2.RequestHandler):
    def get(self):
		
		urlString = self.request.get("stream_key")
		streamKey = ndb.Key(urlsafe=urlString)
		stream_obj = streamKey.get()
		
		images_query = Image.query(ancestor=streamKey).order(-Image.date)
		images = images_query.fetch()
		
		markerDict = {"markers": []}
		for image in images:
			temp = {}
			temp["latitude"] = image.latitude
			temp["longitude"] = image.longitude
			temp["date"] = str(image.date.date())
			temp["url"] = str(image.blob_url)
			markerDict["markers"].append(temp)
					
		# test = {"markers":[
		# { "latitude":57.7973333, "longitude":12.0502107, "title":"Angered", "content":"Representing :)"  , "timestamp":20140110},
		# { "latitude":57.6969943, "longitude":11.9865, "title":"Gothenburg", "content":"Swedens second largest city"  , "timestamp":20140220},
		# { "latitude":57.9969944, "longitude":12.9865, "title":"thing",      "content":"<p>thing1</p><img width='100px' height='100px' src=\"http://localhost:8080/_ah/img/A3W81M_HtEWhUHjywOrEAA==\"/>"  , "timestamp":"20140530"},
		# { "latitude":57.9969944, "longitude":11.9865, "title":"thing2",      "content":"<p>thing2</p><img width='100px' height='100px' src=\"http://localhost:8080/_ah/img/A3W81M_HtEWhUHjywOrEAA==\"/>" , "timestamp":"20141006"}
		# ]}
		
		self.response.write(json.dumps(markerDict))

class MobileView(webapp2.RequestHandler):
	def get(self):
		stream_query = Stream.query(ancestor=stream_key(DEFAULT_STREAM_NAME)).order(Stream.date_added)
		streams = stream_query.fetch()
		
		
		streamUrls = []
		streamKeys = []
		#If no cover_url, sets first (oldest) picture in stream to be cover
		for stream in streams:
			streamUrls.append(stream.cover)
			streamKeys.append(stream.key.urlsafe())
		
		self.response.write(json.dumps({'urls':streamUrls, 'keys':streamKeys}))

class MobileSingle(webapp2.RequestHandler):
	def get(self):
		#urlString = self.request.get("stream_key")
		#streamKey = ndb.Key(urlsafe=urlString)
		#stream_obj = streamKey.get()
		
		#if not post_upload:
		#	stream_obj.viewcount = stream_obj.viewcount + 1
		#	stream_obj.datetime_viewed.append(datetime.datetime.now())
		#	stream_obj.put()
		
		urlString = self.request.get("key")
		streamKey = ndb.Key(urlsafe=urlString)

		#keyDict = parse_qs(urlparse(url).query)
		#streamKey = keyDict['key']
		if(streamKey != ""):
			images_query = Image.query(ancestor=streamKey).order(-Image.date)
			images = images_query.fetch(10)
		
			imageUrls = []
			for image in images:
				imageUrls.append(image.blob_url)
		
			self.response.write(json.dumps(imageUrls))
			
class MobileSearch(webapp2.RequestHandler):
	def get(self):
		search_text = self.request.get("text")
		search_text.lower()
		result_streams = []
		result_urls = []
		streamKeys = []
		num = 0
	
		if search_text != "":
			#search_done = True
			stream_query = Stream.query(ancestor=stream_key(DEFAULT_STREAM_NAME)).order(Stream.date_added)
			streams = stream_query.fetch()
			
			for stream in streams:
				if search_text.lower() in stream.name.lower():
					result_streams.append(stream)
					continue
				for tag in stream.tags:
					if search_text.lower() in tag.lower():
						result_streams.append(stream)
						break
			for stream in result_streams:
				result_urls.append(stream.cover)
				streamKeys.append(stream.key.urlsafe())
			
			num = len(result_urls)
		numArray = [str(num)]
		
		self.response.write(json.dumps({'result': result_urls, 'streamKeys': streamKeys, 'size': numArray}))

class MobileNear(webapp2.RequestHandler):
	def get(self):
		stream_query = Stream.query(ancestor=stream_key(DEFAULT_STREAM_NAME))
		streams = stream_query.fetch()
		
		
		streamUrls = []
		streamKeys = []
		#If no cover_url, sets first (oldest) picture in stream to be cover
		for stream in streams:
			streamUrls.append(stream.cover)
			streamKeys.append(stream.key.urlsafe())
		
		self.response.write(json.dumps({'urls':streamUrls, 'keys':streamKeys}))

class MobileUpload(webapp2.RequestHandler):
	def get(self):
		key = self.request.get("key")
		

		stream_query = Stream.query(ancestor=stream_key(DEFAULT_STREAM_NAME))
		streams = stream_query.fetch()

		name = ""
		for stream in streams:
			if stream.key.urlsafe() == key:
				name = stream.name
		self.response.write(json.dumps(name))
		

application = webapp2.WSGIApplication([
    ('/', FrontPage),
    ('/manage', Manage),
    ('/create', Create),
	('/view', View),
	('/upload', UploadHandler),
	('/streamview', StreamView),
    ('/search', Search),
    ('/trending', Trending),
    ('/error', Error),
	('/cron', Cron),
	('/complete', AutoComplete),
	('/geoview', GeoView),
	('/getgeotags', GetGeoTags),
	('/mobileView', MobileView),
	('/mobileSingle', MobileSingle),
	('/mobileSearch', MobileSearch),
	('/mobileNear', MobileNear),
	('/mobileUpload', MobileUpload)
], debug=True)


		
