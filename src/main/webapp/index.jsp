<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.dev.constants.Constants"%>
<!DOCTYPE html>
<html>
<head>

<title>ETS</title>

<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<link
	href="http://<%=Constants.HOST_IP%>:8080/ets/resources/static/css/bootstrap.min.css"
	rel="stylesheet" />

<style>
.loading {
	width: 100%;
	height: auto;
	display: flex;
	justify-content: center;
	align-items: center;
}

.loading .linebox {
	padding: 2px;
	width: 80%;
	height: 100%;
	border: 2px solid #267591;
	border-radius: 20px;
}

.loading .linebox .line {
	background: #267591;
	height: 20px;
	border-radius: 20px;
	animation: loading 1s forwards cubic-bezier(0, 0, 0, 0);
}

@keyframes loading { 
0% {
	width: 0%;
	}
100%{
width:100%;
	}
}
</style>


<script>
	setTimeout(function() {
		console.log("CALISTI")
		$("#var1").val(true);
		$("#form").submit(); //
	}, 1000);
</script>


</head>

<body style="background-color: #F8F8FF;">
	<form style="display: none"
		action="http://<%=Constants.HOST_IP%>:8080/ets/home" method="POST"
		id="form">
		<input type="hidden" id="var1" name="" value="" /> <input
			type="hidden" id="var2" name="var2" value="" />
	</form>
	<div class="container">
		<div class="row justify-content-center" style="margin-top: 40vh;">
			<div class="col-md-4 col-centered"></div>
			<div class="col-md-4 col-centered">
				<form method="post" action="login"
					style="border-radius: 43px 43px 43px 43px; -moz-border-radius: 43px 43px 43px 43px; -webkit-border-radius: 43px 43px 43px 43px; border: 0px solid #000000; padding: 50px 60px; -webkit-box-shadow: -4px 5px 43px 25px rgba(0, 0, 0, 0.75); -moz-box-shadow: -4px 5px 43px 25px rgba(0, 0, 0, 0.75); box-shadow: -4px 5px 43px 25px rgba(0, 0, 0, 0.75);">

					<div class="row justify-content-center mb-4">
						<p>Loading Home Page</p>

						<div class="loading">
							<div class="linebox">
								<div class="line"></div>
							</div>
						</div>
					</div>


				</form>
			</div>
			<div class="col-md-4 col-centered"></div>
		</div>
	</div>


	<script
		src="http://<%=Constants.HOST_IP%>:8080/ets/resources/static/js/jquery-3.3.1.slim.min.js"></script>

	<script
		src="http://<%=Constants.HOST_IP%>:8080/ets/resources/static/js/popper.min.js"></script>

	<script
		src="http://<%=Constants.HOST_IP%>:8080/ets/resources/static/js/bootstrap.min.js"></script>



</body>
</html>


