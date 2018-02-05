<template>
    <b-row class="mx-0 text-left">
        <b-col cols="2" class="px-0 pt-2 mb-3">*{{fieldName}}:</b-col>
        <template v-for="(emailAddr, index) in mailList">
            <b-col cols="3" class="pr-1 mb-1 pl-0" :key="emailAddr">
                <b-badge variant="secondary" class="pt-1 pl-2 w-100">
                    <span class="email-txt float-left text-left">{{emailAddr}}</span>
                    <span class="text float-right mt-1 close" @click="removeMail(mailList, index)">&times;</span>
                </b-badge>
            </b-col>
            <div class="w-100" v-if="index > 0 && (index + 1) % 3 === 0" :key="index"></div>
            <b-col cols="2" class="px-0 pt-2 mb-3" v-if="index < maxInputNm - 1 && (index > 0 && (index + 1) % 3 === 0)" :key="index * 20"></b-col>
        </template>
        <b-form-group id="inputGroup1" v-if="mailList.length < maxInputNm" class="col pr-0">
            <b-input-group>
                <b-form-input id="input1"
                              @keyup.native="emailMatcher($event)"
                              @blur.native="emailMatcher($event)"
                              :state="fieldValidation"
                              required
                              v-model="inputValue"
                              tabindex="2"
                              placeholder="Enter recipient emails"/>
            </b-input-group>
        </b-form-group>
    </b-row>
</template>

<script>
const EMAIL_REGEX = /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/

export default {
    name: 'EmailListInput',
    props: [
        'mailList',
        'displayMsg',
        'fieldName'
    ],
    data: () => {
        return {
            ccToggle: false,
            bccToggle: false,
            btnDisable: false,
            showMsg: false,
            showLoader: false,
            form: {
                from: '',
                text: '',
                subject: '',
                cc: [],
                bcc: []
            },
            deletePrevFlag: false,
            fieldValidation: null,
            inputValue: '',
            maxInputNm: 9
        }
    },
    methods: {
        removeMail: function (recipientArr, index) {
            recipientArr.splice(index, 1)
            this.fieldValidation = null
        },
        emailMatcher: function (evt) {
            const onBlur = !evt || evt.type === 'blur'
            this.fieldValidation = null
            // catch event for space, enter, comma, semicolon
            // TODO : check this event handling works fine with IE Edge and FF
            this.displayMsg()
            if (onBlur || (evt.target.value && (evt.keyCode === 13 || evt.keyCode === 186 || evt.keyCode === 188 || evt.keyCode === 59 || evt.keyCode === 32))) {
                let value = evt.target.value
                evt.preventDefault()
                if (evt.target.value.match(evt.key + '$') && evt.keyCode !== 13) { // endswith
                    value = evt.target.value.replace(/.$/, '').trim().toLowerCase()
                }

                if (value && EMAIL_REGEX.test(value)) {
                    if (!this.mailList.includes(value)) {
                        this.mailList.push(value)
                    } else {
                        this.displayMsg(`Input email address already exists.`, 'info')
                    }
                    this.inputValue = ''
                    this.fieldValidation = null
                } else {
                    this.fieldValidation = value.length > 0 ? false : null
                }

                if (this.mailList.length > this.maxInputNm - 1) {
                    this.displayMsg(`Limited to ` + this.maxInputNm + ` email addresses for each recipient type.`, 'info')
                }
            } else if (!evt.target.value && evt.keyCode === 8) {
                // backspace
                const length = this.mailList.length
                if (length > 0 && evt.target.value.length === 0 && this.deletePrevFlag) {
                    this.removeMail(this.mailList, length - 1)
                    this.deletePrevFlag = false
                } else if (length > 0 && evt.target.value.length === 0) {
                    this.deletePrevFlag = true
                } else {
                    this.deletePrevFlag = false
                }
            } else {
                this.deletePrevFlag = false
            }
        }
    }
}
</script>
