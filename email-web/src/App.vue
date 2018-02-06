<template>
    <div id="app">
        <div class="logo mb-2">
            <img class="logo-img" src="./assets/logo.jpg">
        </div>
        <div id="hidden-ie-msg">
        </div>
        <router-view/>
    </div>
</template>

<script>
export default {
    name: 'App',
    mounted: function () {
        if (navigator.appName.indexOf('Internet Explorer') > -1) { // IE Check
            let noticeMsg = 'Internet Explorer 9 or below is not supported'
            if (navigator.appVersion.indexOf('MSIE 1') > -1) { // Version 10 or 11
                noticeMsg = 'For better experience, please use modern browsers instead of IE'
            }
            let outDiv = document.createElement('div')
            outDiv.classList.add('row')
            outDiv.classList.add('justify-content-center')
            let innerDiv = document.createElement('div')
            innerDiv.classList.add('alert-warning')
            innerDiv.classList.add('alert')
            innerDiv.textContent = noticeMsg
            outDiv.appendChild(innerDiv)
            document.getElementById('hidden-ie-msg').appendChild(outDiv)
        }
    }
}
</script>
