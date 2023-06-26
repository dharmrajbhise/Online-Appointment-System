<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Certificates</title>
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css" integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU" crossorigin="anonymous">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
    <style>
        
        .navbar {
   			background-color: #606C5D;
        	color: #2B2730;
          /*  background-color: rgba(255, 255, 255, 0.9);
           box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);  */ 
        }

    .navbar-brand {
           color: #2B2730;
        }

    .navbar-nav .nav-link {
            color: #2B2730;
        }

        .container {
            margin-top: 50px;
        }

        .table {
            background-color: #fff;
        }

        .table-header {
            background-color: #ff6b6b;
            color: #fff;
        }

        .table-header th {
            border-color: #ff6b6b;
        }

        .table-row:nth-child(even) {
            background-color: #f2f2f2;
        }

        .table-row:hover {
            background-color: #e0e0e0;
            cursor: pointer;
        }
        
           .container {
      margin-top: 80px;
    }
    
    .container {
      margin-top: 80px;
    }
    .card {
      border: none;
      border-radius: 40px;
      box-shadow: 0 4px 7px rgba(0, 0, 0, 0.1);
      padding: 40px;
      margin-bottom: 20px;
    }
        
        .card-header {
            background-color: #ff6b6b;
            color: #fff;
        }
        h1{
        	color: #ff6b6b;
        }
        
        .card-title {
            margin-bottom: 0;
        }
        
        .btn {
      background-color: #ff6b6b;
      border: none;
      color: #ffffff;
      padding: 8px 16px;
      border-radius: 5px;
      font-size: 14px;
      font-weight: 600;
      transition: background-color 0.3s ease;
    }
    .btn:hover {
      background-color: #ff4f4f;
    }
    </style>
</head>
<body onload="showAlert()">
<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark">
    <a class="navbar-brand" href="#">OAS</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
            aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <a class="nav-link" href="/admin">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/admin/all">All Users</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/admin/messages">Messages</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/logout">logout</a>
            </li>
        </ul>
    </div>
</nav>
<!-- nav end -->
<!-- Filters -->

<div class="container">
	<div class="row">
		<div class="col-md-8 offset-md-2">
		<div class="card border-success">
                    <div class="card-header">
                        <h4 class="card-title text-center">Add Prescription</h4>
                    </div>
                    <div class="card-body">
                    	<form action="/admin/all/certificates/add/${user.username}" method="POST">
				            <div class="form-group">
				                <label for="name">Patient Name:</label>
				                <input type="text" class="form-control" id="name" value="${user.fullName }" name="patientName" required>
				            </div>
				            
				            <div class="form-group">
				                <label for="username">Username:</label>
				                <input type="text" class="form-control" id="username" value="${user.username }" name="username" required>
				            </div>
				            <div class="form-group">
					      <label for="autocomplete-select">Select Username</label>
					      <select id="autocomplete-select" name="certificateName" class="form-control">
					        <option value="Birth Certificate">Birth Certificate</option>
					        <option value="Death Certificate">Death Certificate</option>
					        <option value="Medical Certificate">Medical Certificate</option>
					        <option value="Surgery Certificate">Surgery Certificate</option>
					        <option value="Discharge Certificate">Discharge Certificate</option>
					        <option value="Immunization Certificate">Immunization Certificate</option>
					        <option value="Medical Fitness Certificate">Medical Fitness Certificate</option>
					        <option value="Organ Donor Card">Organ Donor Card</option>
					      </select>
					    </div>
			       
				            
				            <div class="form-group">
				                <label for="date">Issue Date:</label>
				                <input type="date" class="form-control" pattern="\d{4}-\d{2}-\d{2}" id="date" name="issueDate" required>
				            </div>
				           
				            <button type="submit" class="btn btn-primary">Submit</button>
				        </form>
                    </div>
                </div>
		</div>
	</div>
</div>

<h5 class="text-center">${nope}</h5>
<!-- filters end -->


    <div class="container">
        <h2>certificates</h2>

        <table class="table table-responsive mt-4">
            <thead class="table-header">
                <tr>
                    <th>Name</th>
                    <th>Username</th>
                    <th>Certificate Name</th>
                    <th>Issue Date</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="e" items="${certify}">
                <tr class="table-row">
                    <td>${e.patientName }</td>
                    <td>${e.username }</td>
                    <td>${e.certificateName }</td>
                    <td>${e.issueDate }</td>
                    <td>
                    <a href="/admin/all/cerificates/download/${e.id }" class="btn btn-success">Download</a>
                    </td>
                </tr>
               </c:forEach>
                <!-- Add more rows dynamically or retrieve data from backend -->
            </tbody>
        </table>
    </div>
    
<!-- Footer -->
<footer class="text-center text-white" style="background-color: #f1f1f1;">
  <!-- Grid container -->
  <div class="container pt-4">
    <!-- Section: Social media -->
    <section class="mb-4">
      <!-- Facebook -->
      <a
        class="btn btn-link btn-floating btn-lg text-dark m-1"
        href="#!"
        role="button"
        data-mdb-ripple-color="dark"
        ><i class="fab fa-facebook-f"></i
      ></a>

      <!-- Twitter -->
      <a
        class="btn btn-link btn-floating btn-lg text-dark m-1"
        href="#!"
        role="button"
        data-mdb-ripple-color="dark"
        ><i class="fab fa-twitter"></i
      ></a>

      <!-- Google -->
      <a
        class="btn btn-link btn-floating btn-lg text-dark m-1"
        href="#!"
        role="button"
        data-mdb-ripple-color="dark"
        ><i class="fab fa-google"></i
      ></a>

      <!-- Instagram -->
      <a
        class="btn btn-link btn-floating btn-lg text-dark m-1"
        href="#!"
        role="button"
        data-mdb-ripple-color="dark"
        ><i class="fab fa-instagram"></i
      ></a>

      <!-- Linkedin -->
      <a
        class="btn btn-link btn-floating btn-lg text-dark m-1"
        href="#!"
        role="button"
        data-mdb-ripple-color="dark"
        ><i class="fab fa-linkedin"></i
      ></a>
      <!-- Github -->
      <a
        class="btn btn-link btn-floating btn-lg text-dark m-1"
        href="#!"
        role="button"
        data-mdb-ripple-color="dark"
        ><i class="fab fa-github"></i
      ></a>
    </section>
    <!-- Section: Social media -->
  </div>
  <!-- Grid container -->

  <!-- Copyright -->
  <div class="text-center text-dark p-3" style="background-color: rgba(0, 0, 0, 0.2);">
    � 2020 Copyright:
    <a class="text-dark" href="https://OAS.com/">OAS</a>
  </div>
  <!-- Copyright -->
</footer>


<script>
function showAlert() {
    var alertMessage = "${message}";
    if (alertMessage) {
        alert(alertMessage);
    }
}
</script>
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
</body>
</html>