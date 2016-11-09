// Copyright (c) 2012 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// The onClicked callback function.
function onClickHandler(info, tab) {
    var text, url, favIconUrl, isUrl;
    if(info.menuItemId == "context-selection"){
        text = info.selectionText;
        url = info.pageUrl;
        favIconUrl = tab.favIconUrl;
        isUrl = false;
    }else if(info.menuItemId == "context-link"){
        url = info.linkUrl;
        isUrl = true;
    }else if(info.menuItemId == "context-page"){
        text = tab.title;
        url = info.pageUrl;
        favIconUrl = tab.favIconUrl;
        isUrl = true;
    }
    if(url != null){
        console.log("text: " + text + "\nurl: " + url+"\nfavIconUrl: " + favIconUrl + "\nisUrl: " + isUrl);
        saveSentence(text, url, favIconUrl, isUrl);
    }
};

function saveSentence(text, url, favIconUrl, isUrl) {

    $.ajax({
        url: 'http://127.0.0.1:5000/save_sentence',
        type: 'POST',
        data: {'text': text,
            'url': url,
            'favIconUrl': favIconUrl,
            'isUrl': isUrl
        },
        dataType: 'json',
    }).then(function(data){
        // 将正确信息返回content_script
        // sendResponse({'status': 200});
        console.log("data:" + JSON.stringify(data));
        alert("success:" + JSON.stringify(data))
    }, function(e){
        // 将错误信息返回content_script
        // sendResponse({'status': 500});
        console.log("e:" + e);
        alert("failed: " + e);
    });

}

chrome.contextMenus.onClicked.addListener(onClickHandler);

// Set up context menu tree at install time.
chrome.runtime.onInstalled.addListener(function() {
    // Create one test item for each context type.
    var contexts = ["page","selection","link"];
    for (var i = 0; i < contexts.length; i++) {
        var context = contexts[i];
        var title = "Save To Sentences - " + context;
        var id = chrome.contextMenus.create({"title": title, "contexts":[context],
            "id": "context-" + context});
        console.log("'" + context + "' item:" + id);
    }
});
