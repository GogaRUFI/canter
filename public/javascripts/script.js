$('.navbar-collapse a').click(function(){
    $(".navbar-collapse").collapse('hide');
});


$('#createProductForm').submit(function(e) {
    e.preventDefault();

    $.ajax({
        contentType: "application/json; charset=utf-8",
        type: jsRoutes.controllers.ProductApiController.createProduct().type,
        url: jsRoutes.controllers.ProductApiController.createProduct().url,
        data: formToJSONString($('#createProductForm').serializeArray()),
        beforeSend: function(data){
            $('#createProductForm .alert').hide();
        },
        success: function(data){
            $('#createProductForm').get(0).reset();
            var message = generateSuccessAlertMsg();
            $('#createProductForm .alert#success').html(message);
            $('#createProductForm .alert#success').show();
        },
        error: function (request, status, error) {
            var message = generateErrorAlertMsg(request.responseText);
            $('#createProductForm .alert#errors').html(message);
            $('#createProductForm .alert#errors').show();
        }
    });
});

$('#editProductForm').submit(function(e) {
    e.preventDefault();

    $.ajax({
        contentType: "application/json; charset=utf-8",
        type: jsRoutes.controllers.ProductApiController.updateProduct().type,
        url: jsRoutes.controllers.ProductApiController.updateProduct().url,
        data: formToJSONString($('#editProductForm').serializeArray()),
        beforeSend: function(data){
            $('#editProductForm .alert').hide();
        },
        success: function(data){
            var message = generateSuccessAlertMsg();
            $('#editProductForm .alert#success').html(message);
            $('#editProductForm .alert#success').show();
        },
        error: function (request, status, error) {
            var message = generateErrorAlertMsg(request.responseText);
            $('#editProductForm .alert#errors').html(message);
            $('#editProductForm .alert#errors').show();
        }
    });
});


$('#deleteProductButton').click(function(e) {
    e.preventDefault();

    alert("This product will be permanently deleted!");

    $.ajax({
        contentType: "application/json; charset=utf-8",
        type: jsRoutes.controllers.ProductApiController.removeProduct($('#inputId').val()).type,
        url: jsRoutes.controllers.ProductApiController.removeProduct($('#inputId').val()).url,
        beforeSend: function(data){
            $('#editProductForm .alert').hide();
        },
        success: function(data){
            var message = generateSuccessAlertMsg();
            $('#editProductForm .alert#success').html(message);
            $('#editProductForm .alert#success').show();
        },
        error: function (request, status, error) {
            var message = generateErrorAlertMsg(request.responseText);
            $('#editProductForm .alert#errors').html(message);
            $('#editProductForm .alert#errors').show();
        }
    });


});


function generateSuccessAlertMsg(errorResponseText) {
    return "<strong>Success!</strong> Your request has been successfully sent.";
}

function generateErrorAlertMsg(errorResponseText) {
    if (errorResponseText != "") {
        var html = "";
        var errors = JSON.parse(errorResponseText)
        for (var i = 0; i < errors.length; i++){
            html += "<strong>" + errors[i][0] + "</strong> " + errors[i][1]
            if(i != errors.length - 1) {
                html += "<br />"
            }
        }
        return html;
    } else {
        return "<strong>Oops!</strong> Something went wrong, please try again later.";
    }
}

function formToJSONString(formArray) {
    var returnArray = {};
    for (var i = 0; i < formArray.length; i++){
        returnArray[formArray[i]['name']] = formArray[i]['value'];
    }
    return JSON.stringify(returnArray);
}