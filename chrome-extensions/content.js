
console.log("injected");

var resOK = {
    farewell: "content script send response back..."
};

var resError = {
    farewell: "content script hasError!"
};

chrome.extension.onMessage.addListener(function(request, sender, sendResponse) {
    //console.log("Request comes from extention " + sender.tab.url);
    console.log("Request comes from extension: " + JSON.stringify(request.data));

    if (request.data.hello){
        alertify.set('notifier','position', 'top-right');
        alertify.success('Saved to Sentences'+ '\n' + request.data.hello + 'Failed to Save, Please retry later.Failed to Save, Please retry later.Failed to Save, Please retry later.');
        //sendResponse(resOK);
    }else{
        alertify.set('notifier','position', 'top-right');
        alertify.error('Failed to Save, Please retry later.');
        //sendResponse(resError);
    }
});