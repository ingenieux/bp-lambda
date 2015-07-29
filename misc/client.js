// add your definitions here. Or see http://plnkr.co/r823d0VTmvD2qDHuwnVj for a full example

var $ = jQuery;

$(function() {
    $("#btnSubmit").on('click', function() {
        var payload = ($("#commands").text() + "\n").split("\n");

        payload = JSON.stringify(payload);

        console.log("Payload: ", payload);

        AWS.config.credentials.get(function() {
            var lambdaClient = new AWS.Lambda();

            lambdaClient.invoke({
                FunctionName: "bps_opArrayHandler",
                Payload: payload
            }, function(error, lambdaResult) {
                console.log("error: ", error, " lambdaResult: ", lambdaResult);

                if (error) {
                    alert("Error: ", JSON.stringify(error));
                    return;
                }

                var payloadAsHtml = JSON.parse(lambdaResult.Payload);

                payloadAsHtml = "<pre>\n" + payloadAsHtml + "\n</pre>\n";

                $("#commandOutput").html(payloadAsHtml);
            });
        });
    });
});