from google.appengine.ext import blobstore
from google.appengine.ext.webapp import blobstore_handlers
from google.appengine.ext import ndb
from google.appengine.api import images

import webapp2
import cgi
import login
import jinja2
import os
import urllib

MAIN_PAGE_FOOTER_TEMPLATE = """\
    <form action="%s" method="POST" enctype="multipart/form-data">
        Upload File: <input type="file" name="file"><br> <input type="submit" name="submit" value="Submit">
    </form>
    <hr>
    <form>Guestbook name:
      <input value="%s" name="guestbook_name">
      <input type="submit" value="switch">
    </form>
    <a href="%s">%s</a>
  </body>
</html>
"""

DEFAULT_GUESTBOOK_NAME = 'default_guestbook'
###############################################################################################################################################
#Used to get guestbook_name because it is not being passed to Guestbook (This is probably a hack...and when I say probably, I mean absolutely)#
###############################################################################################################################################
GB_NAME =""

def getName():
    return GB_NAME

def setName(name):
    global GB_NAME
    GB_NAME = str(name)
###############################################################################################################################################
###############################################################################################################################################
###############################################################################################################################################
JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

def guestbook_key(guestbook_name=DEFAULT_GUESTBOOK_NAME):
    """Constructs a Datastore key for a Guestbook entity with guestbook_name."""
    return ndb.Key('Guestbook', guestbook_name)

class Greeting(ndb.Model):
    """Models an individual Guestbook entry."""
    author = ndb.UserProperty()
    content = ndb.StringProperty(indexed=False)
    date = ndb.DateTimeProperty(auto_now_add=True)


class View(webapp2.RequestHandler):
    def get(self):
        user = users.get_current_user()

        if user:
            template= JINJA_ENVIRONMENT.get_template('manage.html')
            self.response.write(template.render())
            self.response.write('<html><body>')
            guestbook_name = self.request.get('guestbook_name',
                                              DEFAULT_GUESTBOOK_NAME)

            setName(guestbook_name)

            # Ancestor Queries, as shown here, are strongly consistent with the High
            # Replication Datastore. Queries that span entity groups are eventually
            # consistent. If we omitted the ancestor from this query there would be
            # a slight chance that Greeting that had just been written would not
            # show up in a query.
            greetings_query = Greeting.query(
                ancestor=guestbook_key(guestbook_name)).order(-Greeting.date)
            greetings = greetings_query.fetch(10)

            self.response.write("The uploaded photos by %s are:" % guestbook_name)
            self.response.write('<blockquote>')
            for greeting in greetings:
               # if greeting.author:
               #     self.response.write(
               #             '<b>%s</b> wrote:' % greeting.author.nickname())
               # else:
               #     self.response.write('An anonymous person wrote:')

                # self.response.write('<blockquote>%s</blockquote>' %
                #                    cgi.escape(greeting.content))
                #self.response.write('<blockquote><a href="%s" target="_blank"> <img src="%s" alt="HTML5 Icon" style="width:128px;height:128px"></a></blockquote>' %
                #                    (cgi.escape(greeting.content), cgi.escape(greeting.content)))
                self.response.write('<a href="%s" target="_blank"> <img src="%s" alt="HTML5 Icon" width="200"></a>' %
                                (cgi.escape(greeting.content), cgi.escape(greeting.content)))
            self.response.write('</blockquote>')
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'

            # Write the submission form and the footer of the page
            sign_query_params = urllib.urlencode({'guestbook_name': guestbook_name})

            upload_url = blobstore.create_upload_url('/sign')

            self.response.write(MAIN_PAGE_FOOTER_TEMPLATE %
                                (upload_url, cgi.escape(guestbook_name),
                                 url, url_linktext))

        else: 
            self.redirect('/')

class Guestbook(blobstore_handlers.BlobstoreUploadHandler, webapp2.RequestHandler):
    def post(self):
        # We set the same parent key on the 'Greeting' to ensure each Greeting
        # is in the same entity group. Queries across the single entity group
        # will be consistent. However, the write rate to a single entity group
        # should be limited to ~1/second.
        #guestbook_name = self.request.get('guestbook_name',
        #                                  DEFAULT_GUESTBOOK_NAME)
        guestbook_name = getName()

        greeting = Greeting(parent=guestbook_key(guestbook_name))

        if users.get_current_user():
            greeting.author = users.get_current_user()

        upload_files = self.get_uploads('file')
        blob_info = upload_files[0]
        website = images.get_serving_url(blob_info)
        greeting.content = website
        greeting.put()

        query_params = {'guestbook_name': guestbook_name}
        self.redirect('/view?' + urllib.urlencode(query_params))
        #self.redirect('/view?' + getName())
