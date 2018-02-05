<template>
    <b-container fluid>
        <b-row align-h="center">
            <b-col cols="8">
                <b-alert id="message" :variant="alertMsg.msgType" show>
                    <span>&nbsp;</span>{{alertMsg.message}}
                </b-alert>
            </b-col>
        </b-row>
        <b-row align-h="center">
            <b-col cols="8">
                <b-form id="emailForm" @reset="onReset" class="text-left">
                    <b-form-group horizontal id="inputGroup0" :label-cols="2" label-for="input0" label="*From:">
                        <b-input-group>
                            <b-form-input id="input0" type="email" v-model.trim="form.from"
                                          @change="emailMatch('from', form.from)" :state="inputValidation.from" required
                                          tabindex=1 placeholder="Enter your email"/>
                            <b-btn @click="toggleInput('cc')" :pressed="ccToggle" class="px-3" variant="outline-info">+ CC</b-btn>
                            <b-btn @click="toggleInput('bcc')" :pressed="bccToggle" variant="outline-info">+ BCC</b-btn>
                        </b-input-group>
                    </b-form-group>

                    <b-row class="mx-0 text-left">
                        <b-col cols="2" class="px-0 pt-2 mb-3">*To:</b-col>
                        <template v-for="(toEmail, index) in form.to">
                            <b-col cols="3" class="pr-1 mb-1 pl-0" :key="toEmail">
                                <b-badge variant="secondary" class="pt-1 pl-2 w-100">
                                    <span class="email-txt float-left text-left">{{toEmail}}</span>
                                    <span class="text float-right mt-1 close" @click="removeMail(form.to, index)">&times;</span>
                                </b-badge>
                            </b-col>
                            <div class="w-100" v-if="index > 0 && (index + 1) % 3 === 0" :key="index"></div>
                            <b-col cols="2" class="px-0 pt-2 mb-3" v-if="index < maxInputNm - 1 && (index > 0 && (index + 1) % 3 === 0)" :key="index * 20"></b-col>
                        </template>
                        <b-form-group id="inputGroup1" v-if="form.to.length < maxInputNm" class="col pr-0">
                            <b-input-group>
                                <b-form-input id="input1"
                                              @keyup.native="emailMatcher($event, 'to')"
                                              @blur.native="emailMatcher($event, 'to')"
                                              :state="inputValidation.to"
                                              required
                                              v-model="toInputValue"
                                              tabindex="2"
                                              placeholder="Enter recipient emails"/>
                            </b-input-group>
                        </b-form-group>
                    </b-row>

                    <b-row class="mx-0 text-left" :class="ccToggle ? null : 'd-none'">
                        <b-col cols="2" class="px-0 pt-2 mb-3">CC:</b-col>
                        <template v-for="(ccEmail, index) in form.cc">
                            <b-col cols="3" class="pr-1 mb-1 pl-0" :key="ccEmail">
                                <b-badge variant="secondary" class="pt-1 pl-2 w-100">
                                    <span class="email-txt float-left text-left">{{ccEmail}}</span>
                                    <span class="text float-right mt-1 close" @click="removeMail(form.cc, index)">&times;</span>
                                </b-badge>
                            </b-col>
                            <div class="w-100" v-if="index > 0 && (index + 1) % 3 === 0" :key="index"></div>
                            <b-col cols="2" class="px-0 pt-2 mb-3" v-if="index < maxInputNm - 1 && (index > 0 && (index + 1) % 3 === 0)" :key="index * 20"></b-col>
                        </template>
                        <b-form-group id="inputGroup2" v-if="form.cc.length < maxInputNm" class="col pr-0">
                            <b-input-group>
                                <b-form-input id="input2"
                                              @keyup.native="emailMatcher($event, 'cc')"
                                              @blur.native="emailMatcher($event, 'cc')"
                                              :state="inputValidation.cc"
                                              v-model="ccInputValue"
                                              required
                                              tabindex="3"
                                              placeholder="Enter recipient emails"/>
                            </b-input-group>
                        </b-form-group>
                    </b-row>

                    <b-row class="mx-0 text-left" :class="bccToggle ? null : 'd-none'">
                        <b-col cols="2" class="px-0 pt-2 mb-3">BCC:</b-col>
                        <template v-for="(bccEmail, index) in form.bcc">
                            <b-col cols="3" class="pr-1 mb-1 pl-0" :key="bccEmail">
                                <b-badge variant="secondary" class="pt-1 pl-2 w-100">
                                    <span class="email-txt float-left text-left">{{bccEmail}}</span>
                                    <span class="text float-right mt-1 close" @click="removeMail(form.bcc, index)">&times;</span>
                                </b-badge>
                            </b-col>
                            <div class="w-100" v-if="index > 0 && (index + 1) % 3 === 0" :key="index"></div>
                            <b-col cols="2" class="px-0 pt-2 mb-3" v-if="index < maxInputNm - 1 && (index > 0 && (index + 1) % 3 === 0)" :key="index * 20"></b-col>
                        </template>
                        <b-form-group id="inputGroup3" v-if="form.bcc.length < maxInputNm" class="col pr-0">
                            <b-input-group>
                                <b-form-input id="input3"
                                              @keyup.native="emailMatcher($event, 'bcc')"
                                              @blur.native="emailMatcher($event, 'bcc')"
                                              :state="inputValidation.bcc"
                                              v-model="bccInputValue"
                                              required
                                              tabindex="4"
                                              placeholder="Enter recipient emails"/>
                            </b-input-group>
                        </b-form-group>
                    </b-row>
                    <b-form-group horizontal id="inputGroup4" label-size="sm" :label-cols="2" label-for="input4" label="*Subject:">
                        <b-input-group>
                            <b-form-input id="input4" type="text" v-model="form.subject" :state="inputValidation.subject"
                                          @blur.native="valueValidation('subject')" required tabindex=3 placeholder="Enter Subject"/>
                        </b-input-group>
                    </b-form-group>
                    <b-form-group id="inputGroup5" label-for="textarea1">
                        <b-form-textarea id="textarea1" v-model="form.text" :class="inputValidation.text === true ? 'is-valid' : (inputValidation.text === false ? 'is-invalid' : '')"
                                         tabindex=4 @blur.native="valueValidation('text')" placeholder="Enter something" :rows="3" :max-rows="6">
                        </b-form-textarea>
                    </b-form-group>
                    <div class="float-right">
                        <div class="loader" v-if="showLoader"></div>
                        <b-button variant="info" :disabled="btnDisable" tabindex=5 @click="sendEmail">Submit</b-button>
                        <b-button type="reset" :disabled="btnDisable" tabindex=6>Clear</b-button>
                    </div>
                </b-form>
            </b-col>
        </b-row>
    </b-container>
</template>

<script>
import axios from 'axios'
// EMAIL REGEX to closely match RFC 2822
const EMAIL_REGEX = /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/

axios.defaults.headers.post['Content-Type'] = 'application/json'

export default {
    name: 'EmailSender',
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
                to: [],
                cc: [],
                bcc: []
            },
            deletePrevFlag: {
                bcc: false,
                cc: false,
                to: false
            },
            inputValidation: {
                text: null,
                subject: null,
                from: null,
                to: null,
                cc: null,
                bcc: null
            },
            alertMsg: {
                message: '',
                msgType: 'light'
            },
            toInputValue: '',
            ccInputValue: '',
            bccInputValue: '',
            maxInputNm: 9
        }
    },
    methods: {
        onReset: function (evt) {
            if (evt) {
                // If triggered from ui
                evt.preventDefault()
            }
            this.form.from = ''
            this.form.to = []
            this.form.cc = []
            this.form.bcc = []
            this.form.subject = ''
            this.form.text = ''
            Object.keys(this.inputValidation).forEach((key) => {
                this.inputValidation[key] = null
            })
            this.displayMsg()
        },
        emailMatcher: function (evt, field) {
            const onBlur = !evt || evt.type === 'blur'
            this.inputValidation[field] = null
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
                    if (!this.form.to.includes(value) && !this.form.cc.includes(value) && !this.form.bcc.includes(value)) {
                        this.form[field].push(value)
                    } else {
                        this.displayMsg(`Input email address already exists.`, 'info')
                    }
                    evt.target.value = ''
                    this.inputValidation[field] = null
                } else {
                    this.inputValidation[field] = value.length > 0 ? false : null
                }

                if (this.form[field].length > 10) {
                    this.displayMsg(`Limited to 11 email addresses for each recipient type.`, 'info')
                }
            } else if (!evt.target.value && evt.keyCode === 8) {
                // backspace
                const length = this.form[field].length
                if (length > 0 && evt.target.value.length === 0 && this.deletePrevFlag[field]) {
                    this.removeMail(this.form[field], length - 1)
                    this.deletePrevFlag[field] = false
                } else if (length > 0 && evt.target.value.length === 0) {
                    this.deletePrevFlag[field] = true
                } else {
                    this.deletePrevFlag[field] = false
                }
            } else {
                this.deletePrevFlag[field] = false
            }
            //  EMAIL_REGEX.test(value)
        },
        toggleInput: function (toggleField) {
            if (toggleField === 'bcc') {
                this.bccToggle = !this.bccToggle
                this.form.bcc = []
                this.inputValidation.bcc = null
                this.bccInputValue = ''
            } else {
                this.ccToggle = !this.ccToggle
                this.form.cc = []
                this.inputValidation.cc = null
                this.ccInputValue = ''
            }
        },
        removeMail: function (recipientArr, index) {
            recipientArr.splice(index, 1)
        },
        emailMatch: function (target, value) {
            this.inputValidation[target] = EMAIL_REGEX.test(value)
        },
        valueValidation: function (target) {
            this.inputValidation[target] = !!this.form[target]
        },
        toValidation: function () {
            if (this.form.to.length < 1) {
                this.inputValidation.to = !!this.toInputValue
            }
        },
        sendEmail: function () {
            if (this.inputValidation.from && this.form.to.length > 0 && !!this.form.text && !!this.form.subject) {
                this.displayMsg()
                this.disableBtn(true)
                this.process()
            } else {
                this.emailMatch('from', this.form.from)
                this.toValidation()
                this.valueValidation('text')
                this.valueValidation('subject')
                this.displayMsg('Please check input fields', 'danger')
            }
        },
        displayMsg: function (message, msgType) {
            // to clear message, call without parameters
            this.alertMsg.message = message || ''
            this.alertMsg.msgType = msgType || 'light'

            if (msgType === 'danger') {
                document.getElementById('message').scrollIntoView()
            }
        },
        process: function () {
            const data = JSON.stringify(this.form)
            axios.post(`api/v1/send`, data)
                .then(response => {
                    if (response.status === 200 && response.data && response.data.responseCode === 'SUCCESS') {
                        this.onReset()
                        this.displayMsg('Email request has been submitted successfully', 'success')
                    } else if (response.status === 200) {
                        this.displayMsg(`No servers are available to handle your request. Please try again later.`, 'warning')
                    } else {
                        this.displayMsg(`Something went wrong. Please try again later.`, 'danger')
                    }
                    this.disableBtn(false)
                })
                .catch(e => {
                    this.displayMsg(`Something went wrong.`, 'danger')
                    this.disableBtn(false)
                })
        },
        disableBtn: function (disable) {
            this.btnDisable = disable
            this.showLoader = disable
        }
    }
}
</script>
