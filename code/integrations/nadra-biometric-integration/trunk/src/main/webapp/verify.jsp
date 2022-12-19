<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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

<OBJECT id="OurActiveX" name="OurActiveX" classid="clsid:121C3E0E-DC6E-45dc-952B-A6617F0FAA32" VIEWASTEXT codebase="${pageContext.request.contextPath}/resources/cabinate/OurActiveX.cab"></OBJECT>


<div class="container">

    <div class="starter-template">

        <form:form method="post" class="form-horizontal" id="verification-form" action="${pageContext.request.contextPath}/verify"
                   modelAttribute="verification">

            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="citizenNumber" class="control-label col-sm-4">CNIC</label>
                        <div class="col-sm-8">
                            <form:input path="citizenNumber" id="citizenNumber" cssClass="form-control"/>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="contactNo" class="control-label col-sm-4">Contact No</label>
                        <div class="col-sm-8">
                            <form:input path="contactNo" id="contactNo" cssClass="form-control"/>
                        </div>
                    </div>
                </div>
            </div>


            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label class="control-label col-sm-4" for="fingerIndex">Finger Index</label>
                        <div class="controls text-right col-sm-8">
                            <form:select path="fingerIndex" items="${indexList}" id="fingerIndex"
                                         cssClass="form-control"/>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label class="control-label col-sm-4" for="templateType">Template Type</label>
                        <div class="controls text-right col-sm-8">
                            <form:select path="templateType" items="${templateList}" id="templateType"
                                         cssClass="form-control"/>
                        </div>
                    </div>
                </div>

            </div>


            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label class="control-label col-sm-4" for="areaName">Area Name</label>
                        <div class="controls text-right col-sm-8">
                            <form:select path="areaName" items="${areaList}" id="areaName" cssClass="form-control"/>
                        </div>
                    </div>
                </div>
            </div>





            <div class="row">

                <div class="form-group">
                    <label class="control-label col-sm-4"></label>
                    <div class="text-right col-sm-8">
                        <div id="clearGroup" class="btn-group" role="group" aria-label="">
                            <button type="button" id="capture" name="capture" class="btn btn-primary">
                                Capture
                            </button>
                            <button type="submit" id="submit" name="submit" class="btn btn-success">
                                Verify
                            </button>
                        </div>

                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <div class="form-group text-center">
                        <img id="OutputImage" name="OutputImage" class="img-thumbnail" height="250" src=""/>
                    </div>
                </div>
            </div>

            <form:hidden path="fingerTemplate" id="fingerPrintTemplate" />
        </form:form>


    </div>

</div><!-- /.container -->


<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="resources/js/jquery-3.1.1.min.js"></script>
<script src="resources/js/bootstrap.min.js"></script>

<script>
    $( "#capture" ).click(function() {

        try
        {
            document.OurActiveX.Open(); //Running method from activeX

            //getting parameter to the ActiveX
            var template = document.OurActiveX.MyParam ;
            var image = document.OurActiveX.Image;
            if( template === "" )
            {
                alert("Problem extracting  template at the moment . Please try again after some time");
            }
            else{
                $("#fingerPrintTemplate").val(template);
                $('#OutputImage').attr("src", "data:image/Bmp;base64,"+image);
            }
        }
        catch(Err)
        {
            alert(Err.description);
        }


    });

</script>

</body>
</html>