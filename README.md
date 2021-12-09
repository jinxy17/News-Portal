# Summer 2020 Java Primary School Homework 
## Jin Xuyang 2017013645  Gu Senyao 2018010880

## 1 code structure
The code is improved from the Android Studio template. From the file point of view, it is divided into java code, resources (xml, graph Slice) two parts; from the content point of view, it is divided into the front-end user interface (activity, fragment folder), the back-end system server
Service (service folder) two parts.

The front-end user interface is implemented by Jin Xuyang, with a total of 1244 lines of code. Generally speaking, the program is divided into:

- Startup interface SplashActivity
- The main interface MainActivity
- Browse ExploreFragment
- Classification TypeFragment
- TrendFragment
- KnowledgeFragment
- News content interface NewsDetailActivity
- Tag editing interface TagActivity
- Trending content interface TrendDetailActivity
- Knowledge content interface KnowledgeDetailActivity

Of course, there are many code modules used to complete the user interface logic and provide support, such as various Adapters.

The back-end system service is implemented by Gu Senyao, with a total of 494 lines of code. including:
- Android main program entrance MyApplication
- News browsing/classification service NewsService
- TrendService
- Knowledge Service KnowledgeService

The system service relies on the main program entry MyApplication to provide global support for all user interfaces.


## 2 Implementation
Jin Xuyang:

Division of labor:
1. Writing the front-end user interface

a. SplashActivity: Welcome interface, display pictures, and provide delay time for web page loading.

b. MainActivity: the main interface, you can switch the browse/category/trend/knowledge tab, double-click to go back
exit the program.

  i. ExploreFragment: Apply the Recycler module to display news, support refresh,
  Load more, keyword search, history and other operations.
  
  ii. TypeFragment: Apply Pager and Recycler modules to classify and display, and support various
  Category switching and category additions and deletions.
  
  iii. TrendFragment: Apply Recycler to select domestic/international lists, and support national
  Switch between provinces/countries of the world.
  
  iv. KnowledgeFragment: Get the keywords of the knowledge graph input by the user.

c. NewsDetailActivity: Display the detailed content of news, support page scrolling, call system interface to enter
Row content sharing.

d. TagActivity: call com.beloo.widget.chipslayoutmanager to display active and non-active
Active tags, tag additions and deletions have dynamic effects.

e. TrendDetailActivity: call com.github.mikephil.charting for trend discount
Line graph display, support user zoom and detailed point display.

f. KnowledgeDetailActivity: Display the detailed content of the knowledge graph, support page scrolling, asynchronous
Obtain webpage pictures and create CardView in real time to display relationships and attributes.


2. Use the real machine to test and generate the signed apk file.
Highlights:

1. Each Activity/Fragment is started with each other, and the Intent is used for additional data to transfer data/get the return value.

2. Asynchronously call each Service to obtain network data, and use Handler to implement interface processing after the data arrives.

3. Call online open source packages to display and interact with complex data.

4. Use Photoshop to draw icons with transparent background colors, which is convenient for users to understand.


Gu Senyao:

Division of labor:

1. Write back-end system services

a. MyApplication: The main entrance of the program, three service instances are established to provide global support.

b. NewsService: Asynchronous retrieval/search of news by type/page, and news reading history

Record preservation and restoration.

c. TrendService: Asynchronous acquisition of epidemic trends, and sorted output by province/country.

d. KnowledgeService: Asynchronous field acquisition of knowledge graph.

2. Use the real machine to test

Highlights:

5. The networking takes a long time. In order not to cause the interface to freeze, the networking activities need to be executed in a new thread and passed in
Handler, use Handler to return the result after the execution is completed and the result is obtained.

6. The three system services are encapsulated in accordance with OOP rules, and data exchange uses a unique data structure.
