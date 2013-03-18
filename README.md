WebBoards
=========

<b>Work in progress...</b>

WebBoards is a game engine for playing online adaptations of board wargames.  
Is inspired by [VASSAL](http://www.vassalengine.org/), but allows play diractly via browser.

Goals:
  * Running on desktop and smartphones
  * No software installation 
  * Bookmarkable games
  * Instant updates when your opponent is online
  * Integrated [PBEM](http://en.wikipedia.org/wiki/Play-by-mail_game)-like log support (for I-go U-go games)
  * Game rules implementation

Implementation is based on [SVG](http://en.wikipedia.org/wiki/Scalable_Vector_Graphics), 
[GWT](https://developers.google.com/web-toolkit/) and runs on 
[Google App Engine](https://developers.google.com/appengine/)

The first game being implemented is [MMP](http://www.multimanpublishing.com/Products/tabid/58/ProductID/69/Default.aspx)'s
[Bastogne: Screaming Eagles under Siege](http://boardgamegeek.com/boardgame/35669/bastogne-screaming-eagles-under-siege).

![](https://raw.github.com/wiki/rzymek/webboards/img/screenshot.png)

Contributing
-------------

1. [Fork this repo](https://help.github.com/articles/fork-a-repo)  
2. Push changes to your fork. 
3. [Create a pull request](https://help.github.com/articles/creating-a-pull-request)

To push changes to your fork basicly do:
```bash
git add .               #Mark all local changes for commit
git commit -m "...."    #Commit to local repository
git push                #Push the changes (commits) to the repo at github
```

Remember to keep your [fork in sync](https://help.github.com/articles/syncing-a-fork).

Setting up the development environment 
---------------------------------

You need [maven3](http://maven.apache.org/download.cgi) to build webboards.

1. Clone your fork  
   `git clone https://github.com/$USER/webboards.git`     
2. Start the devserver  
   `mvn appengine:devserver` or `./dev.sh`
3. Go to [http://localhost:8888](http://localhost:8888)  

To generate [Eclipse](http://www.eclipse.org/downloads/) project files call `mvn eclipse:eclipse`.
Then in the IDE select `File > Import > Existing projects into workspace...`

Applying changes
----------------
To apply client side changes call `mvn gwt:compile` and refresh the page.  
To apply changes to resources (jsp, html, etc) call `mvn resources:copy` and refresh the page.  
To apply server side changes restart the `mvn appengine:devserver` command.

Debugging
---------
To debug server side code setup remote debugging to connect to localhost:8000.  
In Eclipse select `Run > Debug configurations... > Remote Java Application`:
![Remote debugging in Eclipse](https://raw.github.com/wiki/rzymek/webboards/img/remote-dbg.png)

To debug client and server code run in GWT DevMode using  
    `mvn gwt:debug`   
(Remember to first stop `mvn appengine:devserver` as it runs on the same ports).  
Then use remote debugging to connect to localhost:8000 (as above).  
When running in GWT DevMode you need to append '&gwt.codesvr=127.0.0.1:9997' to the URL.
You can create a bookmarklet that appends it to the current URL using this:  
    `javascript:window.location=window.location+'&gwt.codesvr=127.0.0.1:9997'`  
The disadvantage of GWT DevMode is that opening webboards page takes a lot longer then using AppEngine DevServer. 
