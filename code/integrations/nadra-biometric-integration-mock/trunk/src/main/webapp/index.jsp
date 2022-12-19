<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="resources/css/bootstrap.css " type="text/css">
    <title>Bio Metric Verification</title>
    <meta http-equiv="X-UA-Compatible" content="IE=8,IE=EmulateIE8,IE=7,IE=EmulateIE7,IE=11,IE=EmulateIE11"/>
</head>

<body>
<OBJECT id="OurActiveX" name="OurActiveX" classid="clsid:121C3E0E-DC6E-45dc-952B-A6617F0FAA32" VIEWASTEXT
        codebase="OurActiveX.cab"></OBJECT>

<div class="container">
    <div class="row text-center center-block" style="width: 80%">
        <!-- Form Name -->
        <legend>Nadra Bio Metric Verification</legend>

        <br>
        <form id="searchForm" method="post" action="${pageContext.request.contextPath}/search">
            <div class="row">
                <div class="col-md-12 text-center">
                    <input type="text" name="searchCnic" id="searchCnic" placeholder="Enter CNIC">
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 text-center">
                    <button name="searchButton" class="btn btn-primary" style="margin-top: 5px" onclick="searchForm">
                        Search Record
                    </button>
                </div>
            </div>
            <br>
        </form>
        <form id="nadraForm" method="post" class="center-block " action="${pageContext.request.contextPath}/submit"
              style="margin-bottom: 30px">
            <fieldset>


                <br>
                <div class="row">
                    <div class="col-md-12 text-center">
                        <div class="col-md-4">
                            <label> Code
                            </label>
                        </div>
                        <div class="col-md-4">
                            <label> Message
                            </label>
                        </div>
                        <div class="col-md-4">
                            <label> SessionID
                            </label>
                        </div>

                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12 text-center">
                        <div>
                            <div class="col-md-4">
                                <input path="responseCode" type="text" name="responseCode" id="responseCode"
                                       value="${nadraIntegrationVO.responseCode}">
                            </div>
                            <div class="col-md-4">
                                <input path="responseDescription" type="text" name="responseDescription"
                                       id="responseDescription" value="${nadraIntegrationVO.responseDescription}">
                            </div>
                            <div class="col-md-4">
                                <input path="sessionId" type="text" name="sessionId" id="sessionId"
                                       value="${nadraIntegrationVO.sessionId}">
                            </div>
                        </div>
                    </div>
                </div>
                <br>
                <div class="row">
                    <div class="col-md-12 text-center">
                        <div class="col-md-4">
                            <label> Citizen Name
                            </label>
                        </div>
                        <div class="col-md-4">
                            <label> Secondary Citizen Number
                            </label>
                        </div>
                        <div class="col-md-4">
                            <label> Secondary Citizen Name
                            </label>
                        </div>

                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12 text-center">
                        <div>
                            <div class="col-md-4">
                                <input path="fullName" type="text" name="fullName" id="fullName"
                                       value="${nadraIntegrationVO.fullName}">

                            </div>
                            <div class="col-md-4">
                                <input path="secondaryContactNo" type="text" name="secondaryContactNo"
                                       id="secondaryContactNo" value="${nadraIntegrationVO.secondaryContactNo}">

                            </div>
                            <div class="col-md-4">
                                <input path="secondaryFullName" type="text" name="secondaryFullName"
                                       id="secondaryFullName" value="${nadraIntegrationVO.secondaryFullName}">

                            </div>
                        </div>
                    </div>
                </div>

                <br>
                <div class="row">
                    <div class="col-md-12 text-center">
                        <div class="col-md-4">
                            <label> Present Address
                            </label>
                        </div>
                        <div class="col-md-4">
                            <label> Birth Place
                            </label>
                        </div>
                        <div class="col-md-4">
                            <label> Date Of Birth
                            </label>
                        </div>

                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12 text-center">
                        <div>
                            <div class="col-md-4">
                                <input path="presentAddress" type="text" name="presentAddress" id="presentAddress"
                                       value="${nadraIntegrationVO.presentAddress}">

                            </div>
                            <div class="col-md-4">
                                <input path="birthPlace" type="text" name="birthPlace" id="birthPlace"
                                       value="${nadraIntegrationVO.birthPlace}">

                            </div>
                            <div class="col-md-4">
                                <input path="dateOfBirth" type="text" name="dateOfBirth" id="dateOfBirth"
                                       value="${nadraIntegrationVO.dateOfBirth}">

                            </div>
                        </div>
                    </div>
                </div>

                <br>
                <div class="row">
                    <div class="col-md-12 text-center">
                        <div class="col-md-4">
                            <label> Card Expired
                            </label>
                        </div>
                        <div class="col-md-4">
                            <label> Finder Index
                            </label>
                        </div>
                        <div class="col-md-4">
                            <label> Religion
                            </label>
                        </div>

                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12 text-center">
                        <div>
                            <div class="col-md-4">
                                <input path="cardExpire" type="text" name="cardExpire" id="cardExpire"
                                       value="${nadraIntegrationVO.cardExpire}">

                            </div>
                            <div class="col-md-4">
                                <input type="text" name="fingerIndex" id="fingerIndex"
                                       value="${nadraIntegrationVO.fingerIndex}">

                            </div>
                            <div class="col-md-4">
                                <input path="religion" type="text" name="religion" id="religion"
                                       value="${nadraIntegrationVO.religion}">

                            </div>
                        </div>
                    </div>
                </div>
                <br>
                <div class="row">
                    <div class="col-md-12 text-center">
                        <div class="col-md-4">
                            <label> Mother Name
                            </label>
                        </div>
                        <div class="col-md-4">
                            <label> Native Language
                            </label>
                        </div>
                        <div class="col-md-4">
                            <label> Photograph
                            </label>
                        </div>

                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12 text-center">
                        <div>
                            <div class="col-md-4">
                                <input path="motherName" type="text" name="motherName" id="motherName"
                                       value="${nadraIntegrationVO.motherName}">

                            </div>
                            <div class="col-md-4">
                                <input path="nativeLanguage" type="text" name="nativeLanguage" id="nativeLanguage"
                                       value="${nadraIntegrationVO.nativeLanguage}">

                            </div>
                            <div class="col-md-4">
                                <input path="photograph" type="text" name="photograph" id="photograph"
                                       value="${nadraIntegrationVO.photograph}">

                            </div>
                        </div>
                    </div>
                </div>
                <br>
                <div class="row">
                    <div class="col-md-12 text-center">
                        <div class="col-md-4">
                            <label> VerificationFunctionality
                            </label>
                        </div>
                        <div class="col-md-4">
                            <label> Enter CNIC
                            </label>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12 text-center">
                        <div>
                            <div class="col-md-4">
                                <input type="text" path="verificationFunctionality" name="verificationFunctionality"
                                       id="verificationFunctionality"
                                       value="${nadraIntegrationVO.verificationFunctionality}">
                            </div>
                            <div class="col-md-4">
                                <input type="text" path="citizenNumber" name="citizenNumber" id="citizenNumber"
                                       value="${nadraIntegrationVO.citizenNumber}" required="required">
                            </div>
                            <div class="col-md-4">
                                <button name="scanButton" class="btn btn-primary" onclick="OpenActiveX()"
                                        for="checkbox-0"> Scan Finger
                                </button>
                                <input type="checkbox" name="checkbox" id="checkbox-0" value="0"
                                       style="margin-left: 5px">
                            </div>
                        </div>
                    </div>
                </div>
                <br>
                <br>
                <%--<img id="OutputImage" name="OutputImage" alt="Finger Print" src="" width="142" height="142"/>--%>
                <Input type="Hidden" name="fingerPrintTemplate" id="fingerPrintTemplate" value="">
                <%--<Input type="Hidden" name="SampleImage" id="SampleImage" value="">--%>
                <div class="row center-block">
                    <!-- Button -->
                    <label class="col-md-4 control-label" for="singlebutton"></label>
                    <div class="col-md-4">
                        <button id="singlebutton" type="submit" name="singlebutton" class="btn btn-primary">Submit
                        </button>
                    </div>
                </div>
    </div>
    </fieldset>
    </form>
</div>

<!-- Attaching to an ActiveX event-->
<script language="javascript">
    window.onload = function () {
        if (navigator.appName == 'Microsoft Internet Explorer' || !!(navigator.userAgent.match(/Trident/) || navigator.userAgent.match(/rv 11/)) || $.browser.msie == 1) {
            // document.getElementById("citizenNumber").onfocusout = OpenActiveX();
        }
        else {
            alert("For now biometric verification is supported with internet explorer only");
        }
    };

    //Passing parameters to ActiveX object and starting application
    function OpenActiveX() {
        try {

            document.OurActiveX.Open(); //Running method from activeX
            //getting parameter to the ActiveX
            var template = document.OurActiveX.MyParam;

            if (template === "") {
                alert("Problem extracting  template at the moment . Please try again after some time");
            }
            else {
                document.getElementById("fingerPrintTemplate").value = template;
                var image = document.OurActiveX.Image;
//                document.getElementById("SampleImage").value = image;
                document.getElementById("checkbox-0").setAttribute("checked", "checked");
                /*document.querySelector("#OutputImage").src ="data:image/Bmp;base64,"+image;*/
                //document.scan.submit();
            }

        }
        catch (Err) {
            alert(Err.description);
        }
    }

    function OurActiveX
    ::OnClose(message)
    {
        alert(message);

    }
    function searchForm() {
        document.getElementById("searchForm").submit();
    }
</script>

</body>
</html>