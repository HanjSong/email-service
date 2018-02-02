<template>
    <b-container fluid>
        <b-row align-h="center">
            <b-col cols="8">
                <b-alert :variant="alertMsg.msgType" show>{{alertMsg.message}}</b-alert>
            </b-col>
        </b-row>
        <b-row align-h="center">
            <b-col cols="8">
                <b-form class="text-left"> <!-- @reset="onReset" v-if="show"-->
                    <b-form-group horizontal id="inputGroup0" :label-cols="2" label-for="input0" label="From :">
                        <b-form-input id="input0" type="email" v-model.trim="from" @change="emailMatch(from)" :state="inputValidation.from" required placeholder="Enter your email"/>
                    </b-form-group>
                    <b-form-group horizontal id="inputGroup1" :label-cols="2" label-for="input1" label="To :">
                        <b-input-group>
                            <!--v-model="form.email"-->
                            <b-form-input id="input1" type="email" required placeholder="Enter recipient email"/>
                            <b-btn @click="ccToggle=!ccToggle" class="px-3" variant="outline-info">CC</b-btn>
                            <b-btn @click="bccToggle=!bccToggle" variant="outline-info">BCC</b-btn>
                        </b-input-group>
                    </b-form-group>
                    <b-form-group horizontal id="inputGroup2" :label-cols="2" label-for="input2" label="CC :" :class="ccToggle ? null : 'd-none'">
                        <b-input-group>
                            <b-form-input id="input2" type="email" required placeholder="Enter email">
                            </b-form-input>
                        </b-input-group>
                    </b-form-group>
                    <b-form-group horizontal id="inputGroup3" :label-cols="2" label-for="input3" label="BCC :" :class="bccToggle ? null : 'd-none'">
                        <b-input-group>
                            <b-form-input id="input3" type="email" required placeholder="Enter email">
                            </b-form-input>
                        </b-input-group>
                    </b-form-group>
                    <b-form-group horizontal id="inputGroup4" :label-cols="2" label-for="input4" label="Subject :">
                        <!-- TODO: reduce text size for small viewport -->
                        <b-input-group>
                            <b-form-input id="input4" type="text" v-model="subject" :state="inputValidation.subject" @blur.native="valueValidation('subject')" required placeholder="Enter Subject"/>
                        </b-input-group>
                    </b-form-group>
                    <b-form-group id="inputGroup5" label-for="textarea1">
                        <b-form-textarea id="textarea1" v-model="content"
                                         :class="inputValidation.content === true ? 'is-valid' :
                                         (inputValidation.content === false ? 'is-invalid' : '')" @blur.native="valueValidation('content')" placeholder="Enter something" :rows="3" :max-rows="6">
                        </b-form-textarea>
                    </b-form-group>
                </b-form>
                <div class="float-right">
                    <b-button variant="info" @click="sendEmail">Submit</b-button>
                    <b-button>Cancel</b-button>
                </div>
            </b-col>
        </b-row>
    </b-container>
</template>

<script>
const EMAIL_REGEX = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i

export default {
    name: 'EmailSender',
    data: () => {
        return {
            ccToggle: false,
            bccToggle: false,
            from: '',
            content: '',
            subject: '',
            inputValidation: {
                content: null,
                subject: null,
                from: null
            },
            to: [],
            cc: [],
            bcc: [],
            alertMsg: {
                message: '',
                msgType: 'success'
            }
        }
    },
    methods: {
        emailMatch: function (target) {
            console.log('checked email format : ', EMAIL_REGEX.test(target))
            this.inputValidation.from = EMAIL_REGEX.test(target)
        },
        valueValidation: function (target) {
            console.log(target, this.inputValidation[target], this[target])
            this.inputValidation[target] = !!this[target]
        },
        sendEmail: function () {
            console.log(this.from)
            if (this.inputValidation.from && !!this.content && !!this.subject) {
                console.log('validation success')
                this.displayMsg('', true) // TODO : remove this line
            } else {
                console.log('validation fail')
                this.displayMsg('Please check input fields', false)
            }
        },
        displayMsg: function (message, success) {
            this.alertMsg.message = message
            this.alertMsg.msgType = success ? 'success' : 'danger'
        }
    }
}
</script>
