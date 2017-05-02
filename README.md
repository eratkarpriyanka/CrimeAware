# CrimeAware
This app allows user to be aware of districts in Chicago having more crimes.

Map to display the individual data (crime incidents) points using your personal Google Maps API key, parameterized for crimes reported within the past 30 days
(Used Retrofit library for API call)
 Used Hashmap to calculate frequencies in O(n)  

Data points markers should be colorized by police district (there are 25 districts)
(Since some districts may have same number of crimes, hence same color shade is used for district with same frequencies.)
Currently, I have set markers manually. This could be optimized by using Marker Clustering. After finding this utility I have used it
for search incident keyword functionality.
Added custom markers.

Ability for the user to search for keywords in the incidents that updates the map 
Used a DB query for this
Added markers using Marker Clustering 

Outline of police district boundaries
By importing the kml file

TODO
Optimization to use Clustering markers for adding shaded markers 
Reducing the load time for maps
UI enhancements

Walkthrough the app CrimeAware

<img src='http://imgur.com/RouyYcH.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />
GIF created with [LiceCap](http://www.cockos.com/licecap/).
