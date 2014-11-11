<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
	<head>
		<title>My Resume</title>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<meta name="description" content="" />
		<meta name="keywords" content="" />
		<script src="js/jquery.min.js"></script>
		<script src="js/jquery.scrolly.min.js"></script>
		<script src="js/skel.min.js"></script>
		<script src="js/init.js"></script>

		<noscript>
			<link rel="stylesheet" href="css/skel.css" />
			<link rel="stylesheet" href="css/style.css" />
			<link rel="stylesheet" href="css/style-desktop.css" />
		</noscript>
	</head>    
    
        <nav id="nav">
            <ul class="container">
        	<li><a href="index.jsp">Home</a></li>
		<li><a href="profolio.jsp">MyPortfolio</a></li>
                <li><a href="works.jsp">works</a></li>
		<li><a href="contact.jsp">Contact</a></li>
            </ul>
	</nav>
            
                    
	<body> 
            <div id="ajax">
                
                
                	<div class="wrapper style1 first">
				<article class="container" id="top">
					<div class="row">
						<div class="4u">
							<span class="image fit"><img src="images/MyPhoto.png" alt="" /></span>
						</div>
						<div class="8u">
							<header>
								<h1>Hi. I'm <strong>David Mao</strong>.</h1>
							</header>
							<p>And this is My Resume, Graduate from <strong>Chung-Hsing University</strong></p>
                                                        <br/>maodavid1989@hotmail.com
                                                        <br/>
                                                        <br/>
                                                                                                                                                                        
						</div>                
					</div>
                                
                                    	<div class="row">
						<div class="row">
<!--                                                    <iframe title="YouTube video player" class="youtube-player" type="text/html" width="520" height="320" 
                                                            src="https://www.youtube.com/embed/m-M1AtrxztU" frameborder="0" allowFullScreen>
                                                    </iframe>
                                                    <iframe title="YouTube video player" class="youtube-player" type="text/html" width="520" height="320" 
                                                            src=" https://www.youtube.com/embed/Ew4VvF0DPMc" frameborder="0" allowFullScreen>
                                                    </iframe>                                                  -->
						</div>
					</div>                      
				</article>
			</div> 
                
            </div>  
	</body>
</html>

    <script>
        $(document).ready(function(e) {
            $("a").click(function(e){
              e.preventDefault();
              var url = $(this).attr("href");
              history.pushState({page:url},url, url);
              updateSite(url);
            });
        });
        
        $(window).bind("popstate", function(e){
            var state = event.state;
            if(state){
                updateSite(state.page);
            }else{
                updateSite("index.jsp");
            }
        });
        
        function updateSite(currentPage){
            $.get(currentPage, function(data) {
                $('#ajax').html(data);
            });
        }

        //iframe 預設瀏覽器高度
        function SetCwinHeight(){
            var iframeid=document.getElementById("iframePage"); //iframe id
            if (document.getElementById){   
                if (iframeid && !window.opera){   
                    if (iframeid.contentDocument && iframeid.contentDocument.body.offsetHeight){   
                        iframeid.height = iframeid.contentDocument.body.offsetHeight;   
                    }else if(iframeid.Document && iframeid.Document.body.scrollHeight){   
                        iframeid.height = iframeid.Document.body.scrollHeight;   
                    }   
                }
            }
        }

    </script>
    