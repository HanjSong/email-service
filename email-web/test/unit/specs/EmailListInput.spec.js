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
})
