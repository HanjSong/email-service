<template>
    <div id="app">
        <div class="logo mb-2">
            <img class="logo-img" src="./assets/logo.jpg">
        </div>
        <div id="hidden-ie-msg"></div>
        <router-view/>
    </div>
</template>

<script>
export default {
    name: 'App',
    created: function () {
        if (navigator.appName.indexOf("Internet Explorer") != -1) { // IE Check
            let noticeMsg = 'Internet Explorer 9 or below is not supported'
            if (navigator.appVersion.indexOf("MSIE 1") > -1) { // Version 10 or 11
                noticeMsg = 'For better experience, please use modern browsers instead of IE'
            }

            let ieNoticeHtml = `<div class="row justify-content-center"><div class="mt-3 col-8">` +
            `<div role="alert" aria-live="polite" aria-atomic="true" class="alert alert-danger" id="message">` +
            noticeMsg + `</div></div></div>`

            document.getElementById('hidden-ie-msg').innerHTML = ieNoticeHtml
        }
    }
}
</script>
