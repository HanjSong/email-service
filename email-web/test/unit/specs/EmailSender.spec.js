import Vue from 'vue'
import EmailSender from '@/components/EmailSender'

describe('Test EmailSender', () => {
    let EmailComponent = null

    beforeEach(() => {
        const Constructor = Vue.extend(EmailSender)
        EmailComponent = new Constructor().$mount()
    })

    it('should render correct contents', () => {
        expect(EmailComponent.$el.querySelector('#inputGroup0').textContent).to.contain('*From')
        expect(EmailComponent.$el.querySelector('#inputGroup4').textContent).to.contain('*Subject')
    })
})
