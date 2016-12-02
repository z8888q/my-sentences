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
        url: 'http://138.128.208.218:5000/save_sentence',
        type: 'POST',
        data: {'text': text,
            'url': url,
            'favIconUrl': favIconUrl,
            'isUrl': isUrl
        },
        dataType: 'json',
    }).then(function(data){
        console.log("data:" + JSON.stringify(data));

        chrome.tabs.query({active: true, currentWindow: true}, function(tabs) {
            lastTabId = tabs[0].id;
            chrome.tabs.sendMessage(lastTabId, {
                'data': data
            }, function(response) {
                console.log(response);
            });
        });
    }, function(e){
        console.log("e:" + e);
        chrome.tabs.query({active: true, currentWindow: true}, function(tabs) {
            lastTabId = tabs[0].id;
            chrome.tabs.sendMessage(lastTabId, {
                'status': 500
            }, function(response) {
                console.log(response.farewell);
            });
        });
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
