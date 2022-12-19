<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="resources/css/bootstrap.css " type="text/css">
    <title>Bio Metric Verification</title>
</head>

<body>
<OBJECT id="OurActiveX" name="OurActiveX" classid="clsid:121C3E0E-DC6E-45dc-952B-A6617F0FAA32" VIEWASTEXT codebase="OurActiveX.cab"></OBJECT>

<div class="container">

    <form method="post" class="center-block " action="${pageContext.request.contextPath}/fingerPrintVerificationWeb" onsubmit="return post();">
        <fieldset>
            <div class="row text-center center-block" style="width: 80%">

                <!-- Form Name -->
                <legend>Nadra Bio Metric Verification</legend>

                <div class="row">
                    <div class="col-md-12 text-center">
                        <div>
                            <label> Enter CNIC
                                <input type="text" name="citizenNumber" id="citizenNumber" onblur="OpenActiveX()">
                            </label>
                        </div>
                        <div class="checkbox ">
                            <label for="checkbox-0">
                                <input type="checkbox" name="checkbox" id="checkbox-0" value="0">
                                Finger Print
                            </label>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12 text-center">
                        <div>
                            <label>Enter Mobile No
                                <input type="text" name="contactNo" id="contactNo" />
                            </label>
                        </div>
                        <div class="checkbox ">
                            <label>Enter Finger Index
                                <input type="text" name="fingerIndex" id="fingerIndex" />
                            </label>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12 text-center">
                        <div>
                            <label>Enter Transaction Id
                                <input type="text" name="serviceProviderTransactionId" id="serviceProviderTransactionId" value="" />
                            </label>
                        </div>
                    </div>
                </div>
                <Input type="Hidden" name="fingerPrintTemplate" id="fingerPrintTemplate" value="">
                <Input type="Hidden" name="SampleImage" id="SampleImage" value="">
                <div class="row center-block">
                    <!-- Button -->
                    <label class="col-md-4 control-label" for="singlebutton"></label>
                    <div class="col-md-4">
                        <button id="singlebutton" type="submit" name="singlebutton" class="btn btn-primary">Submit</button>
                    </div>
                </div>
            </div>
        </fieldset>
    </form >
</div>

<!-- Attaching to an ActiveX event-->
<script language="javascript">


    window.onload = function() {
        if (navigator.appName == 'Microsoft Internet Explorer' ||  !!(navigator.userAgent.match(/Trident/) || navigator.userAgent.match(/rv 11/)) || $.browser.msie == 1)
        {
           // document.getElementById("citizenNumber").onfocusout = OpenActiveX();
        }
        else{
            alert("For now biometric verification is supported with internet explorer only");
        }
    };

    //Passing parameters to ActiveX object and starting application
    function OpenActiveX()
    {
        try
        {

            document.OurActiveX.Open(); //Running method from activeX
            //getting parameter to the ActiveX
            var template = document.OurActiveX.MyParam ;

            if( template === "" )
            {
                alert("Problem extracting  template at the moment . Please try again after some time");
            }
            else{
                document.getElementById("fingerPrintTemplate").value = template ;
                var image = document.OurActiveX.Image;
                document.getElementById("SampleImage").value = image ;
                document.getElementById("checkbox-0").setAttribute("checked","checked");
                /*document.querySelector("#OutputImage").src ="data:image/Bmp;base64,"+image;*/
                //document.scan.submit();
            }

        }
        catch(Err)
        {
            alert(Err.description);
        }
    }

    function OurActiveX::OnClose(message)
    {
        alert(message);

    }
</script>

    </body>
</html>