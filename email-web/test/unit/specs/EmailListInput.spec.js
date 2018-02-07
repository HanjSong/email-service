import Vue from 'vue'
import EmailListInput from '@/components/EmailListInput'

describe('Test EmailListInput', () => {
    let EmailListComponent = null

    beforeEach(() => {
        const Constructor = Vue.extend(EmailListInput)
        const props = {
            mailList: [],
            displayMsg: function () {

            },
            fieldName: 'To'
        }
        EmailListComponent = new Constructor({propsData: props}).$mount()
    })

    it('should render correct contents', () => {
        expect(EmailListComponent.$el.querySelector('.field-name').textContent).to.contain('*To:')
    })

    it('should validate email address in input', () => {
        const input = EmailListComponent.$el.querySelector('.email-list-input')
        input.value = 'abcde@mail.com'

        const blurEvent = new window.Event('blur')
        input.dispatchEvent(blurEvent)
        EmailListComponent._watcher.run()

        expect(EmailListComponent.mailList[0]).to.eq('abcde@mail.com')
    })

    it('should not add same email address in input', () => {
        const input = EmailListComponent.$el.querySelector('.email-list-input')
        input.value = 'abcde@mail.com'

        const blurEvent = new window.Event('blur')
        input.dispatchEvent(blurEvent)
        EmailListComponent._watcher.run()

        input.value = 'abcde@mail.com'
        input.dispatchEvent(blurEvent)
        EmailListComponent._watcher.run()

        expect(EmailListComponent.mailList[0]).to.eq('abcde@mail.com')
        expect(EmailListComponent.mailList.length).to.eq(1)
    })

    it('should validate email address in input until limit', () => {
        let assertCalled = false
        EmailListComponent.displayMsg = function () {
            assertCalled = true
        }
        const input = EmailListComponent.$el.querySelector('.email-list-input')
        const maxNm = EmailListComponent.maxInputNm
        const blurEvent = new window.Event('blur')

        for (let i = 0; i < maxNm; i++) {
            input.value = 'abcde_' + i + '@mail.com'
            input.dispatchEvent(blurEvent)
            EmailListComponent._watcher.run()
        }
        expect(EmailListComponent.mailList[0]).to.eq('abcde_0@mail.com')
        expect(EmailListComponent.mailList[maxNm - 1]).to.eq('abcde_' + (maxNm - 1) + '@mail.com')
        expect(assertCalled).to.eq(true)
    })

    it('should not add invalid email address in input', () => {
        const input = EmailListComponent.$el.querySelector('.email-list-input')
        input.value = 'abcde_invalid_mail.com'

        const blurEvent = new window.Event('blur')
        input.dispatchEvent(blurEvent)
        EmailListComponent._watcher.run()

        expect(EmailListComponent.mailList.length).to.eq(0)
    })

    it('should clear input when triggered', () => {
        const input = EmailListComponent.$el.querySelector('.email-list-input')
        input.value = 'somevalue'

        const blurEvent = new window.Event('blur')
        input.dispatchEvent(blurEvent)
        EmailListComponent._watcher.run()

        EmailListComponent.clearField()

        expect(EmailListComponent.mailList.length).to.eq(0)
        expect(EmailListComponent.inputValue).to.eq('')
        expect(EmailListComponent.fieldValidation).to.eq(null)
    })

    it('should remove email address when back key pressed continuously', () => {
        const input = EmailListComponent.$el.querySelector('.email-list-input')
        input.value = 'abcde@mail.com'

        const blurEvent = new window.Event('blur')
        input.dispatchEvent(blurEvent)
        EmailListComponent._watcher.run()

        input.value = 'abcde2@mail.com'
        input.dispatchEvent(blurEvent)
        EmailListComponent._watcher.run()

        const keyEvent = new window.Event('keyup')
        keyEvent.keyCode = 8
        // if value is not empty, prev item will not be deleted
        input.value = 'v'
        input.dispatchEvent(keyEvent)
        input.dispatchEvent(keyEvent)
        expect(EmailListComponent.mailList.length).to.eq(2)

        input.value = ''
        input.dispatchEvent(keyEvent)
        // still not deleted
        expect(EmailListComponent.mailList.length).to.eq(2)
        input.dispatchEvent(keyEvent)
        // If delete was input again, now delete
        expect(EmailListComponent.mailList.length).to.eq(1)
    })

    it('should remove email address when close is clicked', () => {
        const input = EmailListComponent.$el.querySelector('.email-list-input')
        input.value = 'abcde@mail.com'

        const blurEvent = new window.Event('blur')
        input.dispatchEvent(blurEvent)
        EmailListComponent._watcher.run()

        input.value = 'toberemoved@mail.com'
        input.dispatchEvent(blurEvent)
        EmailListComponent._watcher.run()

        input.value = 'edcba@mail.com'
        input.dispatchEvent(blurEvent)
        EmailListComponent._watcher.run()

        expect(EmailListComponent.mailList[1]).to.eq('toberemoved@mail.com')

        EmailListComponent.removeMail(1)
        expect(EmailListComponent.mailList[1]).to.eq('edcba@mail.com')
    })
})
