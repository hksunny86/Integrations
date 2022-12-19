<%@page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Bio Metric Verification</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="resources/css/bootstrap.css " type="text/css">

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="resources/css/starter-template.css " type="text/css">
</head>

<body>

<div class="container" modelAttribute="verification">

    <div class="starter-template">

        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label for="responseCode" class="control-label col-sm-4">Response Code</label>
                    <div class="col-sm-8">
                        <input class="form-control" name="responseCode" id="responseCode"
                               value="${verification.responseCode}" readonly>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label for="responseDescription" class="control-label col-sm-4">Response Message</label>
                    <div class="col-sm-8">
                        <input class="form-control" name="responseDescription" id="responseDescription"
                               value="${verification.responseDescription}" readonly>
                    </div>
                </div>
            </div>
        </div>


        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label for="sessionId" class="control-label col-sm-4">Session Id</label>
                    <div class="col-sm-8">
                        <input class="form-control" name="sessionId" id="sessionId"
                               value="${verification.sessionId}" readonly>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label for="citizenNumber" class="control-label col-sm-4">CNIC</label>
                    <div class="col-sm-8">
                        <input class="form-control" name="citizenNumber" id="citizenNumber"
                               value="${verification.citizenNumber}" readonly>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label for="fullName" class="control-label col-sm-4">Full Name</label>
                    <div class="col-sm-8">
                        <input class="form-control" name="fullName" id="fullName"
                               value="${verification.fullName}" readonly>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label for="presentAddress" class="control-label col-sm-4">Present Address</label>
                    <div class="col-sm-8">
                        <input class="form-control" name="presentAddress" id="presentAddress"
                               value="${verification.presentAddress}" readonly>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label for="birthPlace" class="control-label col-sm-4">Birth Place</label>
                    <div class="col-sm-8">
                        <input class="form-control" name="birthPlace" id="birthPlace"
                               value="${verification.birthPlace}" readonly>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label for="cardExpire" class="control-label col-sm-4">Card Expired</label>
                    <div class="col-sm-8">
                        <input class="form-control" name="cardExpire" id="cardExpire"
                               value="${verification.cardExpire}" readonly>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">

            <div class="col-md-6">
                <div class="form-group">
                    <label for="dateOfBirth" class="control-label col-sm-4">Date Of Birth</label>
                    <div class="col-sm-8">
                        <input class="form-control" name="dateOfBirth" id="dateOfBirth"
                               value="${verification.dateOfBirth}" readonly>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="form-group">
                    <label for="fingerIndex" class="control-label col-sm-4">Finger Index</label>
                    <div class="col-sm-8">
                        <input class="form-control" name="fingerIndex" id="fingerIndex"
                               value="${verification.fingerIndex}" readonly>
                    </div>
                </div>
            </div>
        </div>


    </div>

</div><!-- /.container -->


<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="resources/js/jquery-3.1.1.min.js"></script>
<script src="resources/js/bootstrap.min.js"></script>


</body>
</html>