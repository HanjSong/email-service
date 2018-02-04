<template>
    <b-container fluid>
        <b-row align-h="center">
            <b-col cols="8">
                <b-alert :variant="alertMsg.msgType" show>
                    <span>&nbsp;</span>{{alertMsg.message}}
                </b-alert>
            </b-col>
        </b-row>
        <b-row align-h="center">
            <b-col cols="8">
                <b-form id="emailForm" @reset="onReset" class="text-left">
                    <b-form-group horizontal id="inputGroup0" :label-cols="2" label-for="input0" label="*From:">
                        <b-form-input id="input0" type="email" v-model.trim="form.from"
                                      @change="emailMatch('from', form.from)" :state="inputValidation.from" required
                                      tabindex=1 placeholder="Enter your email"/>
                    </b-form-group>
                    <b-form-group horizontal id="inputGroup1" :label-cols="2" label-for="input1" label="*To:">
                        <b-input-group>
                            <b-form-input id="input1" type="email" v-model.trim="form.to[0]"
                                          @change="emailMatch('to', form.to[0])" :state="inputValidation.to" required
                                          tabindex=2 placeholder="Enter recipient email"/>
                            <b-btn @click="ccToggle=!ccToggle" :pressed="ccToggle" class="px-3" variant="outline-info">CC</b-btn>
                            <b-btn @click="bccToggle=!bccToggle" :pressed="bccToggle" variant="outline-info">BCC</b-btn>
                        </b-input-group>
                    </b-form-group>
                    <b-form-group horizontal id="inputGroup2" :label-cols="2" label-for="input2" label="CC :" :class="ccToggle ? null : 'd-none'">
                        <b-input-group>
                            <b-form-input id="input2" type="email" v-model.trim="form.cc[0]" @change="emailMatch('cc', form.cc[0])" required placeholder="Enter email">
                            </b-form-input>
                        </b-input-group>
                    </b-form-group>
                    <b-form-group horizontal id="inputGroup3" :label-cols="2" label-for="input3" label="BCC :" :class="bccToggle ? null : 'd-none'">
                        <b-input-group>
                            <b-form-input id="input3" type="email" v-model.trim="form.bcc[0]" @change="emailMatch('bcc', form.bcc[0])" required placeholder="Enter email">
                            </b-form-input>
                        </b-input-group>
                    </b-form-group>
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
// EMAIL REGEX from W3C
const EMAIL_REGEX = /^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/

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
                from: 'idefix.song@gmail.com',
                text: 'test',
                subject: 'test',
                to: ['idefix.song@gmail.com'],
                cc: [],
                bcc: []
            },
            inputValidation: {
                text: true,
                subject: true,
                from: true,
                to: true
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
            this.displayMsg()
        },
        emailMatch: function (target, value) {
            this.inputValidation[target] = EMAIL_REGEX.test(value)
        },
        valueValidation: function (target) {
            this.inputValidation[target] = !!this.form[target]
        },
        sendEmail: function () {
            if (this.inputValidation.from && this.inputValidation.to && !!this.form.text && !!this.form.subject) {
                this.displayMsg()
                this.disableBtn(true)
                this.process()
            } else {
                this.emailMatch('from', this.form.from)
                this.emailMatch('to', this.form.to)
                this.valueValidation('text')
                this.valueValidation('subject')
                this.displayMsg('Please check input fields', 'danger')
            }
        },
        displayMsg: function (message, msgType) {
            // to clear message, call without parameters
            this.alertMsg.message = message || ''
            this.alertMsg.msgType = msgType || 'light'
        },
        process: function () {
            const data = JSON.stringify(this.form)
            axios.post(`api/v1/send`, data)
                .then(response => {
                    console.log(response, response.status === 200, response.data, response.data.responseCode)
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
