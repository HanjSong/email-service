import Vue from 'vue'
import EmailSender from '@/components/EmailSender'

describe('Test EmailSender', () => {
    let EmailComponent = null

    const responseFromServer = {
        status: 200,
        data: {
            responseCode: 'SUCCESS'
        }
    }

    /**
     * This mock server ajax call will return given mockResponse as response
     * and mock try/catch behavior when forceError value is set to true
     * @param mockResponse
     * @param forceError
     * @returns {Function}
     */
    const mockAjaxCall = function (mockResponse, forceError) {
        return function () {
            return {
                then: function (func) {
                    mockResponse = mockResponse || responseFromServer
                    func(mockResponse)
                    return {
                        catch: (errorCallback) => {
                            if (forceError) {
                                errorCallback()
                            }
                        }
                    }
                }
            }
        }
    }

    beforeEach(() => {
        const Constructor = Vue.extend(EmailSender)
        EmailComponent = new Constructor().$mount()
    })

    afterEach(() => {
        EmailComponent.$destroy()
    })

    it('should render correct contents', () => {
        expect(EmailComponent.$el.querySelector('#inputGroup0').textContent).to.contain('*From')
        expect(EmailComponent.$el.querySelector('#inputGroup4').textContent).to.contain('*Subject')
    })

    it('should toggle cc and bcc when btn is clicked', () => {
        const ccbtn = EmailComponent.$el.querySelector('#addCCBtn')
        // Initial state will be false
        expect(EmailComponent.ccToggle).to.eq(false)
        const clickEvent = new window.Event('click')
        ccbtn.dispatchEvent(clickEvent)
        EmailComponent._watcher.run()
        expect(EmailComponent.ccToggle).to.eq(true)

        const bccbtn = EmailComponent.$el.querySelector('#addBCCBtn')
        // Initial state will be false
        expect(EmailComponent.bccToggle).to.eq(false)
        bccbtn.dispatchEvent(clickEvent)
        EmailComponent._watcher.run()
        expect(EmailComponent.bccToggle).to.eq(true)
    })

    it('should validate from email address on change', () => {
        const input = EmailComponent.$el.querySelector('#input0')
        input.value = 'fromnotvalidmail.com'

        const changeEvent = new window.Event('change')
        input.dispatchEvent(changeEvent)
        EmailComponent._watcher.run()

        expect(EmailComponent.inputValidation.from).to.eq(false)
    })

    it('should validate subject on blur', () => {
        const subject = EmailComponent.$el.querySelector('#input4')
        EmailComponent.form.subject = ''

        const blurEvent = new window.Event('blur')
        subject.dispatchEvent(blurEvent)
        EmailComponent._watcher.run()

        expect(EmailComponent.inputValidation.subject).to.eq(false)

        EmailComponent.form.subject = 'subjecttext'
        subject.dispatchEvent(blurEvent)
        EmailComponent._watcher.run()

        expect(EmailComponent.inputValidation.subject).to.eq(true)
    })

    it('should validate comment on blur', () => {
        const comment = EmailComponent.$el.querySelector('#textarea1')
        EmailComponent.form.text = ''

        const blurEvent = new window.Event('blur')
        comment.dispatchEvent(blurEvent)
        EmailComponent._watcher.run()

        expect(EmailComponent.inputValidation.text).to.eq(false)

        EmailComponent.form.text = 'commenttext'
        comment.dispatchEvent(blurEvent)
        EmailComponent._watcher.run()

        expect(EmailComponent.inputValidation.text).to.eq(true)
    })

    it('should validate all fields and display message when clicked', () => {
        const submitBtn = EmailComponent.$el.querySelector('#submitBtn')

        const clickEvent = new window.Event('click')
        submitBtn.dispatchEvent(clickEvent)
        EmailComponent._watcher.run()

        expect(EmailComponent.inputValidation.text).to.eq(false)
        expect(EmailComponent.inputValidation.subject).to.eq(false)
        expect(EmailComponent.inputValidation.from).to.eq(false)
        expect(EmailComponent.alertMsg.message).to.eq('Please check input fields')
        expect(EmailComponent.alertMsg.msgType).to.eq('danger')
    })

    it('should successfully call process when submit is clicked', () => {
        EmailComponent.form.from = 'sender@mail.com'
        EmailComponent.form.to = ['some@mail.com']
        EmailComponent.form.subject = 'subject'
        EmailComponent.form.text = 'text'
        EmailComponent.inputValidation.from = true

        EmailComponent.getData = mockAjaxCall()

        const submitBtn = EmailComponent.$el.querySelector('#submitBtn')
        const clickEvent = new window.Event('click')
        submitBtn.dispatchEvent(clickEvent)
        EmailComponent._watcher.run()

        // loading image should be hidden after success
        expect(EmailComponent.showLoader).to.eq(false)
        expect(EmailComponent.alertMsg.message).to.eq('Email request has been submitted successfully')
        expect(EmailComponent.alertMsg.msgType).to.eq('success')
    })

    it('should process success response and display success msg', () => {
        EmailComponent.getData = mockAjaxCall()

        EmailComponent.process()
        expect(EmailComponent.alertMsg.message).to.eq('Email request has been submitted successfully')
        expect(EmailComponent.alertMsg.msgType).to.eq('success')
    })

    it('should process success response and display notice msg when email send fails on server side', () => {
        EmailComponent.getData = mockAjaxCall({
            status: 200,
            data: {
                responseCode: 'ERROR'
            }
        })

        EmailComponent.process()
        expect(EmailComponent.alertMsg.message).to.eq('No servers are available to handle your request. Please try again later.')
        expect(EmailComponent.alertMsg.msgType).to.eq('warning')
    })

    it('should process success response and display error msg when server side exceptions has happened', () => {
        EmailComponent.getData = mockAjaxCall({
            status: 500,
            data: {
                responseCode: 'ERROR'
            }
        })

        EmailComponent.process()
        expect(EmailComponent.alertMsg.message).to.eq('Something went wrong. Please try again later.')
        expect(EmailComponent.alertMsg.msgType).to.eq('danger')
    })

    it('should process success response and display error msg when unknown error(exceptions) has happened', () => {
        EmailComponent.getData = mockAjaxCall(null, true)

        EmailComponent.process()
        expect(EmailComponent.alertMsg.message).to.eq('Something went wrong.')
        expect(EmailComponent.alertMsg.msgType).to.eq('danger')
    })
})
