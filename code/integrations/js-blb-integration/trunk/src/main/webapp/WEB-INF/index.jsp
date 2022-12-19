<!DOCTYPE html>

<html>
    <head>
        <title>Start Page</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
    <form class="form-horizontal" method="post" action="${pageContext.request.contextPath}/send-bulk-sms">
        <fieldset>

            <!-- Form Name -->
            <legend>SEND SMS</legend>
            <br>
            <!-- File Button -->
            <div class="form-group">
                <label class="col-md-4 control-label" for="browsBtn"></label>
                <div class="col-md-4">
                    <input id="browsBtn" name="browsBtn" class="input-file" type="file">
                </div>
            </div>
            <br>
            <!-- Button -->
            <div class="form-group">
                <label class="col-md-4 control-label" for="uploadBtn"></label>
                <div class="col-md-4">
                    <button id="uploadBtn" name="uploadBtn" class="btn btn-primary" type="submit">Upload</button>
                </div>
            </div>
            <br>
            <!-- Button -->
            <div class="form-group">
                <label class="col-md-4 control-label" for="sendBtn"></label>
                <div class="col-md-4">
                    <button id="sendBtn" name="sendBtn" class="btn btn-primary"type="button" disabled>SEND</button>
                </div>
            </div>
            <br>
            <!-- Button -->
            <div class="form-group">
                <label class="col-md-4 control-label" for="downloadBtn"></label>
                <div class="col-md-4">
                    <button id="downloadBtn" name="downloadBtn" class="btn btn-primary" type="button" disabled>Download</button>
                </div>
            </div>

        </fieldset>
    </form>
    </body>
</html>
