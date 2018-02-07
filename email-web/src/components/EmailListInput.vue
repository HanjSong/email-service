<template>
    <b-row class="mx-0 text-left">
        <b-col cols="2" class="px-0 pt-2 mb-3 field-name">*{{fieldName}}:</b-col>
        <template v-for="(emailAddr, index) in mailList">
            <b-col cols="3" class="pr-1 mb-1 pl-0" :key="emailAddr">
                <b-badge variant="secondary" class="pt-1 pl-2 w-100">
                    <span class="email-txt float-left text-left">{{emailAddr}}</span>
                    <span class="text float-right mt-1 close" @click="removeMail(index)">&times;</span>
                </b-badge>
            </b-col>
            <div class="w-100" v-if="index > 0 && (index + 1) % 3 === 0" :key="index"></div>
            <b-col cols="2" class="px-0 pt-2 mb-3" v-if="index < maxInputNm - 1 && (index > 0 && (index + 1) % 3 === 0)" :key="index + 1"></b-col>
        </template>
        <b-form-group v-if="mailList.length < maxInputNm" class="col email-list pr-0">
            <b-input-group>
                <b-form-input @keyup.native="emailMatcher($event)"
                              @blur.native="emailMatcher($event)"
                              :state="fieldValidation"
                              required
                              v-model="inputValue"
                              tabindex="2"
                              class="email-list-input"
                              placeholder="Enter recipient emails"/>
            </b-input-group>
        </b-form-group>
    </b-row>
</template>

<script>
export default {
    name: 'EmailListInput',
    props: [
        'mailList',
        'displayMsg',
        'fieldName',
        'required'
    ],
    data: () => {
        return {
            deletePrevFlag: false,
            fieldValidation: null,
            inputValue: '',
            maxInputNm: 9
        }
    },
    created: function () {
        this.$eventHub.$on('email-list-validation', this.validateField)
        this.$eventHub.$on(this.fieldName.toLowerCase() + '-email-list-clear', this.clearField)
        this.$eventHub.$on('clear-email-form', this.clearField)
    },
    methods: {
        clearField: function () {
            this.fieldValidation = null
            this.inputValue = ''
        },
        validateField: function () {
            if (this.required && this.mailList.length < 1) {
                this.fieldValidation = this.$globalStore.EMAIL_REGEX.test(this.inputValue)
            }
        },
        removeMail: function (index) {
            this.mailList.splice(index, 1)
            this.fieldValidation = null
        },
        emailMatcher: function (evt) {
            const onBlur = !evt || evt.type === 'blur'
            this.fieldValidation = null
            // catch event for space, enter, comma, semicolon
            this.displayMsg()
            if (onBlur ||
                (evt.target.value && (evt.keyCode === 13 || evt.keyCode === 186 || evt.keyCode === 188 ||
                    evt.keyCode === 59 || evt.keyCode === 32))) {
                let value = evt.target.value.trim().replace(/[.,;]$/, '').trim().toLowerCase()

                if (value && this.$globalStore.EMAIL_REGEX.test(value)) {
                    let isExisting = false
                    this.mailList.forEach((itm) => {
                        if (itm === value) {
                            isExisting = itm === value
                        }
                    })

                    if (!isExisting) {
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
                    this.removeMail(length - 1)
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
