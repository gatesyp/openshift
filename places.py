from types import GeneratorType

import responses

import test as _test
import googlemaps




key = 'AIzaSyD2Hsf8_41hwc7EZSw_VMsjwBbIWP4S9nA'
client = googlemaps.Client(key)
location = (-33.86746, 151.207090)
type = 'liquor_store'
language = 'en-AU'
radius = 1000
url = 'https://maps.googleapis.com/maps/api/place/textsearch/json'
responses.add(responses.GET, url,body='{"status": "OK", "results": [], "html_attributions": []}',
  status=200, content_type='application/json')

print client.places('restaurant', location=location,
 radius=radius, language=language,
 min_price=1, max_price=4, open_now=True,
 type=type)

