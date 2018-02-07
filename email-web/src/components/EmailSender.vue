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
                            <b-btn id="addCCBtn" @click="toggleCCInput" :pressed="ccToggle" class="px-3" variant="outline-info">+ CC</b-btn>
                            <b-btn id="addBCCBtn" @click="toggleBCCInput" :pressed="bccToggle" variant="outline-info">+ BCC</b-btn>
                        </b-input-group>
                    </b-form-group>

                    <email-list-input
                        ref="toInput"
                        :mailList="form.to"
                        fieldName="To"
                        :displayMsg="displayMsg"
                        required="true">
                    </email-list-input>

                    <email-list-input
                        ref="ccInput"
                        :mailList="form.cc"
                        fieldName="CC"
                        :class="ccToggle ? null : 'd-none'"
                        :displayMsg="displayMsg">
                    </email-list-input>

                    <email-list-input
                        ref="bccInput"
                        :mailList="form.bcc"
                        fieldName="BCC"
                        :class="bccToggle ? null : 'd-none'"
                        :displayMsg="displayMsg">
                    </email-list-input>

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
                        <b-button id="submitBtn" type="button" @click="sendEmail" variant="info" :disabled="showLoader" tabindex=5>Submit</b-button>
                        <b-button type="reset" :disabled="showLoader" tabindex=6>Clear</b-button>
                    </div>
                </b-form>
            </b-col>
        </b-row>
    </b-container>
</template>

<script>
import axios from 'axios'
import EmailListInput from './EmailListInput.vue'

axios.defaults.headers.post['Content-Type'] = 'application/json'

export default {
    components: {EmailListInput},
    name: 'EmailSender',
    data: () => {
        return {
            ccToggle: false,
            bccToggle: false,
            showLoader: false,
            form: {
                from: '',
                text: '',
                subject: '',
                to: [],
                cc: [],
                bcc: []
            },
            inputValidation: {
                text: null,
                subject: null,
                from: null
            },
            alertMsg: {
                message: '',
                msgType: 'light'
            }
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
            this.$eventHub.$emit('clear-email-form')
            this.displayMsg()
        },
        toggleBCCInput: function (toggleField) {
            this.bccToggle = !this.bccToggle
            this.form.bcc = []
            this.$eventHub.$emit('bcc-email-list-clear')
        },
        toggleCCInput: function (toggleField) {
            this.ccToggle = !this.ccToggle
            this.form.cc = []
            this.$eventHub.$emit('cc-email-list-clear')
        },
        removeMail: function (recipientArr, index) {
            recipientArr.splice(index, 1)
        },
        emailMatch: function (target, value) {
            this.inputValidation[target] = this.$globalStore.EMAIL_REGEX.test(value)
        },
        valueValidation: function (target) {
            this.inputValidation[target] = !!this.form[target]
        },
        toValidation: function () {
            this.$eventHub.$emit('email-list-validation')
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
                if (this.$el.querySelector('#message').scrollIntoView) {
                    this.$el.querySelector('#message').scrollIntoView()
                } else {
                    // work around for only IE 10
                    window.scrollTo(0, 0)
                }
            }
        },
        process: function () {
            const data = JSON.stringify(this.form)
            this.getData(data)
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
        getData: function (data) {
            return axios.post(`api/v1/send`, data)
        },
        disableBtn: function (disable) {
            this.showLoader = disable
        }
    }
}
</script>
