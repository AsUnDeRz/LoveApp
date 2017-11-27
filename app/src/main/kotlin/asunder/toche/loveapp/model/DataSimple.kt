package asunder.toche.loveapp

import android.databinding.ObservableArrayList

/**
 * Created by admin on 8/12/2017 AD.
 */
object DataSimple {

    val textAboutEn ="Welcome to LoveApp, a user friendly online platform where users will, through the service, be able to do the followings:\n" +
            "·           Access to news and information about HIV/ AIDS, STIs, TB, sexual health and other related issues for prevention, treatment, or care and support,\n" +
            "·           Test your knowledge on how much you know about HIV/ AIDS, STIs, TB, and sexual health as well as determine your HIV risk through simple questions of the HIV Risk Meter,\n" +
            "·           Search for nearby health and HIV-related service availability to get testing and counselling,\n" +
            "·           Set up your own personal reminders for testing appointment, medication, and prescription refill, and it is able to be connected with Apple Watch in order to make more channel for reminders,\n" +
            "·           Record number of CD4 and Viral Load for user with HIV positive, so you can track the progress on your health during the treatment,\n" +
            "·           Secure access to your privacy on LoveApp with Passcode Lock and Touch ID.\n" +
            "LoveApp is free to download, and all users can access to the service. Download LoveApp now and take care of your health. For more information about LoveApp, directly contact us at love.application@raksthai.org\n"
    val textAboutTh ="ยินดีต้อนรับสู่ LoveApp พื้นที่ออนไลน์ที่เป็นมิตรต่อผู้ใช้บริการ โดยผู้ใช้บริการสามารถเข้าถึงบริการต่างๆของ LoveAppได้ ดังนี้\n" +
            "·           เข้าถึงข่าวสารและข้อมูลที่เกี่ยวข้องกับเฮชไอวี/ เอดส์ โรคติดต่อทางเพศสัมพันธ์ วัณโรค สุขภาวะทางเพศ และประเด็นต่างๆที่เกี่ยวข้อง เพื่อการป้องกันโรค การรักษา หรือการดูแลหลังติดเชื้อ\n" +
            "·           ทดสอบความรู้เกี่ยวกับเฮชไอวี/ เอดส์ โรคติดต่อทางเพศสัมพันธ์ วัณโรค และสุขภาวะทางเพศ รวมถึงทดสอบความเสี่ยงต่อการติดเชื้อเฮชไอวีผ่านคำถามง่ายๆในชุดวัดความเสี่ยงเฮชไอวี (HIV Risk Meter)\n" +
            "·           ค้นหาหน่วยบริการทางด้านสุขภาพและเฮชไอวีที่ใกล้คุณ เพื่อเข้ารับบริการตรวจและการให้การปรึกษา\n" +
            "·           ตั้งเครื่องช่วยเตือนความจำส่วนตัว เพื่อให้คุณไม่พลาดนัดหมายการตรวจ การทานยา หรือการเติมยาตามใบสั่งแพทย์ และยังสามารถเชื่อมต่อกับ Apple Watch เพื่อเพิ่มช่องทางการเตือนได้อีกด้วย\n" +
            "·           บันทึกจำนวนเม็ดเลือดขาว (CD4) และปริมาณไวรัสในเลือด (Viral Load) สำหรับผู้ที่ติดเชื้อเฮชไอวี เพื่อช่วยให้คุณได้ติดตามการเปลี่ยนแปลงที่เกิดขึ้นในระหว่างการรักษา\n" +
            "·           รักษาความปลอดภัยในการเข้าใช้งานด้วยระบบ Passcode Lock และ Touch ID\n" +
            "ผู้ใช้บริการสามารถดาวน์โหลด LoveApp ได้โดยไม่มีค่าใช้จ่าย หากต้องการข้อมูลเพิ่มเติมเกี่ยวกับ LoveApp สามารถติดต่อเราโดยตรงได้ที่ Email: love.application@raksthai.org\n"
    val textCondition = "The terms and conditions of use shown here (hereinafter referred to as the \"Terms and Conditions\") set forth the terms between Raks Thai Foundation (hereinafter referred to as the \"Raks Thai Foundation\", “We”, “our”, or “us” depending upon context) and users (hereinafter referred to as the \"User\" or \"Users\") of any services or features of a mobile application named “LoveApp” and its software and website (hereinafter referred to as the \"Service\"), which are developed and operated by Raks Thai Foundation. By accessing and using the Service, Users acknowledge to bound by and comply with the following Terms and Conditions:\n" +
            " \n" +
            "1.           Purpose of Service\n" +
            "This Service is designed, developed, and operated under the Stop TB and AIDS through RTTR program funded by the Global Fund to Fight AIDS, Tuberculosis, and Malaria, to be a user friendly online platform for the following purposes:\n" +
            "1.1.    Providing health and HIV-related information and education for prevention,\n" +
            "1.2.    Primarily recruiting individual at risk for HIV, STIs, and/ or TB, and referring them to get Voluntary Confidential Counselling and Testing (VCCT) and/ or treatment,\n" +
            "1.3.    Retaining people living with HIV in care and treatment, and people having HIV, STIs, and/ or TB related risk behaviours in prevention.\n" +
            "This Service is free to download, and all users can access to the Service.\n" +
            " \n" +
            "2.           Scope of Service\n" +
            "Through the Service, Users will be able to do the followings:\n" +
            "2.1.    Access to news and information about HIV/ AIDS, STIs, TB, sexual health and other related issues for prevention, treatment, or care and support,\n" +
            "2.2.    Test their knowledge on how much they know about HIV/ AIDS, STIs, TB, and sexual health as well as determine their HIV risk through simple questions of the HIV Risk Meter,\n" +
            "2.3.    Search for nearby health and HIV-related service availability to get testing, counselling, and/ or treatment,\n" +
            "2.4.    Set up their own personal reminders for testing appointment, medication, and prescription refill, and it is able to be connected with Apple Watch in order to make more channel for reminders,\n" +
            "2.5.    Record number of CD4 and Viral Load for users with HIV positive, so they can track the progress on their health during the treatment,\n" +
            "2.6.    Secure access to their privacies on the Service with Passcode Lock and Touch ID,\n" +
            "2.7.    Rate the Service, and provide feedback for improvement,\n" +
            "2.8.    Experience other features and functions as provided on the Services.\n" +
            "Although we will use our expertise with caution in performing the Service, we do not verify and do not guarantee that all information provided is accurate, complete, correct, or the latest available, and we are not responsible for any errors (including placement and typing errors), obstruction (either because of temporary and/or partial, damage, repair or improvement to the site or otherwise), inaccuracy, misleading or false information or non-delivery of information.\n" +
            " \n" +
            "Raks Thai Foundation reserves the right to modify or cease, at its sole discretion, the whole or part of the Service at any time without any prior notice to Users.\n" +
            " \n" +
            "3.           Acceptance of Terms and Conditions Agreement\n" +
            "3.1.    All Users shall use the Service in accordance with the terms stated in the Terms and Conditions. Users may not use the Service unless they agree to the Terms and Conditions. Such agreement is valid, enforceable, and irrevocable,\n" +
            "3.2.    By actually using the Service, Users are deemed to have agreed to the Terms and Conditions. Such agreement is valid, enforceable, and irrevocable,\n" +
            "3.3.    If there is any changes or modifications made to the Terms and Conditions for the Service, Users also shall comply with such changes or modifications as well as the Terms and Conditions.\n" +
            " \n" +
            "4.           Modification of the Terms and Conditions\n" +
            "Raks Thai Foundation may, in its sole discretion, modify the Terms when we deems necessary, with or without providing prior notice to Users. The modification will become effective immediately once the modified Terms are posted on an appropriate location within the Service offered by Raks Thai Foundation. Users shall be deemed to have granted valid and irrevocable consent to the modified Terms and Conditions by continuing to use the Service. Users shall refer to the Terms and Conditions on a regular basis when using the Service, since a separate notification regarding the modification to Terms and Conditions may not be provided.\n" +
            " \n" +
            "5.           User Account\n" +
            "5.1.    To access and use the Service, Users must register for a LoveApp account (hereinafter referred to as the \"Account\") by providing accurate, complete, and up-to-date information indicated as required. Users are responsible to promptly update such information if it changes,\n" +
            "5.2.    To protect the Account, Users shall create login credentials by using a strong password and limiting its use to this Account. Users are responsible for keeping their password secure. Raks Thai Foundation cannot and will not be liable for any loss or damage from any failures to maintain the security of Users’ Account and password,\n" +
            "5.3.    Users are entirely responsible for any and all usages or activities that occur under the Account,\n" +
            "5.4.    Each Account in the Service is for the exclusive use and belongs solely to the owner of the Account. Users may not transfer or lend their Accounts to any third party nor may their Accounts be inherited by any third party,\n" +
            "5.5.    Users acknowledge that Raks Thai Foundation might use the email address users provided as the primary method for communication,\n" +
            "5.6.    Raks Thai Foundation, in its sole discretion, may suspend or delete a User's Account without giving prior notice to User if we find that User is violating or has violated the Terms and Conditions.\n" +
            " \n" +
            "6.           Privacy\n" +
            "Raks Thai Foundation places its highest priority on the privacy of its Users. We respectfully values Users’ confidentiality and private right. Our collection and use of personal and other information about Users is subject to our Privacy Policy. Raks Thai Foundation promises to protect the privacy and personal information of its users in accordance with our Privacy Policy, and we will endeavour to comply with the requirements of relevant data protection legislation when performing its obligations under these Terms and Conditions. However, Users understand that, through accessibility and use of the Service, Users consent to the collection and use (as set forth in the Privacy Policy) of this information.\n" +
            " \n" +
            "7.           Healthcare and Medical Information\n" +
            "Any healthcare and medical information posted and referred to in and through this Service is solely intended for informational purposes only and is not intended to replace consultation with a qualified medical practitioner, or to endorse any specific treatment, healthcare, or other service providers. Users should not use the information contained herein for diagnosing a health problem or disease as this Service is not designed to and does not provide medical advice, professional diagnosis, opinion, or treatment to users or to any other individual. Users are strongly advised to talk to an appropriate professional for specific advice tailored to their situations.\n" +
            " \n" +
            "8.           Content on the Service and Third Party Link\n" +
            "8.1.    All materials published on the Service (including, but not limited to news, articles, data, text, graphics, photographs, images, illustrations, also known as the \"Content\") are protected by copyright, and owned or controlled by Raks Thai Foundation or the party credited as the provider of the Content. Users shall abide by all additional copyright notices, information, or restrictions contained in any Content accessed through the Service,\n" +
            "8.2.    Content on the Service made by Raks Thai Foundation are collected from various sources purposed to provide information, knowledge, and advantage to Users. We use our best effort to regularly collect correct and up-to-date information used for providing the Content. However, by using the Service, Users may encounter Content or information that might be inaccurate, incomplete, delayed, misleading, offensive, or otherwise harmful. We cannot always prevent this error of our Service, so we make no representations or warranties of any kind that our Content published or posted on the Service are reliable, correct and update. Users are borne to the entire risks from the use of the Service,\n" +
            "8.3.    The Service may include links to other web sites or services solely as a convenience to Users. Users will have the ability to access and/ or use content provided by third parties and links to websites and services maintained by third parties. Raks Thai Foundation is not responsible for others’ content or information, and we disclaim any responsibility or liability related to users’ accessibility or use of such third party content,\n" +
            "8.4.    Accessing and using of linked sites, including information, material, products, and services on linked sites or available through linked sites is solely at Users’ own risk. Each service provider has different terms and conditions of use, so Users should always check the terms and conditions of use posted on Third-Party sites.\n" +
            " \n" +
            "9.           User Responsibility\n" +
            "Users shall use this Service at their own risk, and shall bear all responsibility for actions carried out and their results upon this Service. Raks Thai Foundation may take measures that we considers necessary and appropriate, if we acknowledges that a User is using the service in a way which violates the Terms and Conditions. However, Raks Thai Foundation shall not be responsible for correcting or preventing such violation towards Users or others.\n" +
            " \n" +
            "10.  \tIntellectual Property Rights\n" +
            "Raks Thai Foundation holds the intellectual property rights on this Service. All information and materials, including but not limited to software, text, data, graphics, images, logos, icons, ideas, concepts, interfaces, source code, and other codes on its online platform are prohibited to be published, modified, copied, reproduced, duplicated, or altered in any way outside the area of this Service without the express written permission of Raks Thai Foundation. If Users violate these rights, Raks Thai Foundation reserves the right to bring a civil claim for the full amount of damages or losses suffered. These violations may also constitute criminal offences.\n" +
            " \n" +
            "11.  \tUsage Restriction\n" +
            "Users shall not use the Service or the Content for any purpose that is unlawful, unauthorized, or prohibited by these Terms and Conditions. Users shall not use any equipment, software, or other technologies that may obstruct or attempt to obstruct the operation of this Service, and Users agree not to seek, create, search for, use or send automated agents or other forms of technology to collect or obtain information from this Service, or otherwise interact with this Service.\n" +
            " \n" +
            "12.  \tTermination\n" +
            "Raks Thai Foundation reserves the right, in its sole discretion, to terminate this agreement or user’s Account to access to any or all of the Service and the related services or any portion thereof at any time with or without cause or notice.\n" +
            " \n" +
            "13.  \tException of Liability\n" +
            "13.1.        Raks Thai Foundation does not expressly or impliedly guarantee that the Service including the Content are free from de facto or legal flaws (including but not limited to stability, reliability, accuracy, integrity, effectiveness, fitness for certain purposes, security-related faults, errors, bugs, or infringements of rights). Raks Thai Foundation shall not be responsible for providing the Service without such defects.\n" +
            "13.2.        Raks Thai Foundation shall not be responsible for any damages inflicted upon Users in relation to the use of the Service.\n" +
            "13.3.        Raks Thai Foundation shall not be responsible for any indirect, special, incidental, consequential or punitive damages (including but not limited to such damages that the Company or Users predicted or could have predicted) with respect to the Company's contractual default or act of tort due to the Company's negligence (except for gross negligence).\n" +
            " \n" +
            "12.  \tGoverning Law\n" +
            "In the event of any dispute, controversy or claim arising under or in connection with use of the Service under these Terms and Connections, Users agree that such dispute, controversy, and claim shall be governed by and construed in accordance with the laws of the Kingdom of Thailand laws and the court of Thailand.\n" +
            " \n" +
            "13.  \tContact Information\n" +
            "Should Users have any questions or concerns with respect to these Terms and Conditions of Use or the Service, Users can directly contact us via email at the following address: love.application@raksthai.org."


    val textPrivacy ="Raks Thai Foundation, as a Principal Recipient of the Stop TB and AIDS through RTTR (STAR) program funded by the Global Fund to Fight AIDS, Tuberculosis, and Malaria, hereby declares its privacy policy and confidentiality (hereinafter referred to as the \"Privacy Policy\") to safeguard confidential information of users or individual who accesses to its service.\n" +
            " \n" +
            "1.           Scope and Purpose of this Privacy Policy\n" +
            "This Privacy Policy describes the practices of Raks Thai Foundation to process and safeguard your personal information, and the rights and choices you have with respect to your personal information. This Privacy Policy applies to personal information you provide us when you use the LoveApp mobile application and its website (hereinafter referred to as the \"Service\"), and information we collect when you use any online service maintained by us relating to the LoveApp. By accessing and using the Service, you expressly consent to the collection, use, and disclosure of your personal information in accordance with this Privacy Policy.\n" +
            " \n" +
            "2.           Respectfulness on your privacy\n" +
            "Raks Thai Foundation respects your privacy right, and commits that your activities made via the Service will be protected with highest standard of security. The information will, thus, be utilised solely for the specific purposes indicated in this Privacy Policy, and we will proceed with rigorous security measures as well as prevent data from any unauthorised utilisation without obtaining your prior permission.\n" +
            " \n" +
            "3.           Correction and Retention of Information\n" +
            "The information gathered when you interact with the Services falls into two categories as follow;\n" +
            "I.             Personal Information\n" +
            "The personally-identifiable information will be collected when you create a LoveApp account in order to access and use the Service. You will be required to provide the unique identifier code (composed of first letter of first name, first letter of last name, two digit of date of birth, two digit of month of birth, and two digit of year of birth), gender identity, current province, and nationality. Apart from the above, the other information requested on the Service such as email, location, and feedback massage, is optional for you to complete your person profile whilst using the Service. We shall not collect personally-identifiable information without your consent, and you also maintain a right of rectification with regard to personal information and may modify or delete your personal information, in whole or in part, at any time.\n" +
            "II.          Non-personal Information\n" +
            "The Service automatically collects non-personally-identifiable information through technology or using the cookies to monitor user usage patterns and information. This non-personally-identifiable is included but not limited to your internet protocol (“IP”) address, geographic location data, operating system type, time and date stamps, mostly viewed pages, search preferences, as well as other generic internet usage related data.\n" +
            "Raks Thai Foundation will collect and retain such information if we consider that such collection will be of benefits to the STAR program or to you in order for you to obtain good Service or opportunities from us. Such information is to be used by Raks Thai Foundation for the its administering and maintaining your account records, for the its being in compliance with the relevant laws, rules and regulations, for its improvement and development of the Service, or for having more understanding of your needs, so that Raks Thai Foundation may then invent and develop its services at a higher satisfactory level to you.\n" +
            " \n" +
            "4.           Rights and Choices of User\n" +
            "Subject to applicable law, you may have a right to access your personal information, to have errors in your personal information rectified, and to object to the processing of your personal information on legitimate grounds. In addition, if you change your mind regarding how much information you wish to continue to share or disclose on the Service going forward, you can always change the settings in the application. \n" +
            " \n" +
            "5.           Use of Information\n" +
            "Raks Thai Foundation will analyse online patterns and usage trends of users with the general information collected. We reserve the right to use personal information in order to tailor the Service to your specific needs, offer information that may be of interest to you, and for information analysis and other programmatic purposes.\n" +
            " \n" +
            "6.           Disclosure of Information\n" +
            "You have privacy right over your information. Raks Thai Foundation will not disclose your information in respect of your account to any persons other than its authorized officers and affiliates and any third persons authorized by you to access to such information, and we will also protect your information from being used without obtaining your prior permission. However, Raks Thai Foundation reserves the right to disclose any information, including personally-identifiable information, when required by law or when necessary to investigate potential illegal activities such as fraud, violations of the Terms of Use, or when otherwise necessary to protect the property, rights, and safety of the Service, users, or other third parties.\n" +
            " \n" +
            "7.           Cookies\n" +
            "A cookie is a small text files stored on your device whilst using the Service. It helps store preferences and record session information. Cookies enhance your experience on the Service. Cookies do not collect other data from a device’s hard drive. You have the option to enable and disable cookies; however, you may not be able to use certain features and functions of the Service if you disable cookies. Unless you have adjusted the browser setting so that it will refuse cookies, our system will issue cookies when you access to the Service.\n" +
            " \n" +
            "8.           Applicable Law\n" +
            "This Privacy Policy shall be governed by and construed in accordance with the laws of the Kingdom of Thailand laws and the court of Thailand.\n" +
            " \n" +
            "9.           Amendments of this Privacy Policy\n" +
            "Raks Thai Foundation may, in its sole discretion, amend this Privacy Policy at any time, and such amendments will be effective immediately once the modified Privacy Policy is provided or posted to the Service offered by Raks Thai Foundation. By continuing to use the Service, you will be deemed as acceptance of the modified Privacy Policy, so please check the update from time to time.\n"

    //val imageHome = arrayListOf(R.drawable.icon_40,R.drawable.icon_36,R.drawable.icon_41)
    //var imageHome = arrayListOf<String>()
    var imageHome = arrayListOf<Model.ImageHome>()
    val iconGame1 by lazy {
        arrayListOf(
                R.drawable.mm1,R.drawable.mm2,R.drawable.mm3,R.drawable.mm4,R.drawable.mm5,
                R.drawable.mm6,R.drawable.mm7,R.drawable.mm8,R.drawable.mm9,R.drawable.mm10,
                R.drawable.mm11,R.drawable.mm11,R.drawable.mm13,R.drawable.mm14,R.drawable.mm15,
                R.drawable.mm16,R.drawable.mm17,R.drawable.mm18,R.drawable.mm19,R.drawable.mm20,
                R.drawable.mm21,R.drawable.mm22,R.drawable.mm23,R.drawable.mm24,R.drawable.mm25,
                R.drawable.mm26,R.drawable.mm27,R.drawable.mm28,R.drawable.mm29,R.drawable.mm30,
                R.drawable.mm31,R.drawable.mm31,R.drawable.mm33,R.drawable.mm34,R.drawable.mm35,
                R.drawable.mm36,R.drawable.mm37,R.drawable.mm38,R.drawable.mm39,R.drawable.mm40,
                R.drawable.mm41,R.drawable.mm42,R.drawable.mm43,R.drawable.mm44,R.drawable.mm45,
                R.drawable.mm46,R.drawable.mm47,R.drawable.mm48,R.drawable.mm49,R.drawable.mm50,
                R.drawable.mm51,R.drawable.mm51,R.drawable.mm53,R.drawable.mm54,R.drawable.mm55,
                R.drawable.mm56,R.drawable.mm57,R.drawable.mm58,R.drawable.mm59,R.drawable.mm60,
                R.drawable.mm61,R.drawable.mm62,R.drawable.mm63,R.drawable.mm64,R.drawable.mm65,
                R.drawable.mm66,R.drawable.mm67,R.drawable.mm68,R.drawable.mm69,R.drawable.mm70
        )
    }

    val iconGame2 by lazy {
        arrayListOf(
                R.drawable.m2m1,R.drawable.m2m2,R.drawable.m2m3,R.drawable.m2m4,R.drawable.m2m5,
                R.drawable.m2m6,R.drawable.m2m7,R.drawable.m2m8,R.drawable.m2m9,R.drawable.m2m10,
                R.drawable.m2m11,R.drawable.m2m12,R.drawable.m2m13,R.drawable.m2m14,R.drawable.m2m15,
                R.drawable.m2m16,R.drawable.m2m17,R.drawable.m2m18,R.drawable.m2m19,R.drawable.m2m20,
                R.drawable.m2m21,R.drawable.m2m22,R.drawable.m2m23,R.drawable.m2m24,R.drawable.m2m25,
                R.drawable.m2m26,R.drawable.m2m27,R.drawable.m2m28,R.drawable.m2m29,R.drawable.m2m30,
                R.drawable.m2m31,R.drawable.m2m32,R.drawable.m2m33,R.drawable.m2m34,R.drawable.m2m35,
                R.drawable.m2m36,R.drawable.m2m37,R.drawable.m2m38,R.drawable.m2m39,R.drawable.m2m40,
                R.drawable.m2m41,R.drawable.m2m42,R.drawable.m2m43,R.drawable.m2m44,R.drawable.m2m45,
                R.drawable.m2m46,R.drawable.m2m47,R.drawable.m2m48,R.drawable.m2m49,R.drawable.m2m50,
                R.drawable.m2m51,R.drawable.m2m52,R.drawable.m2m53,R.drawable.m2m54,R.drawable.m2m55,
                R.drawable.m2m56,R.drawable.m2m57,R.drawable.m2m58,R.drawable.m2m59,R.drawable.m2m60,
                R.drawable.m2m61,R.drawable.m2m62,R.drawable.m2m63,R.drawable.m2m64,R.drawable.m2m65,
                R.drawable.m2m66,R.drawable.m2m67,R.drawable.m2m68,R.drawable.m2m69,R.drawable.m2m70,
                R.drawable.m2m71,R.drawable.m2m72,R.drawable.m2m73,R.drawable.m2m74,R.drawable.m2m75,
                R.drawable.m2m76,R.drawable.m2m77,R.drawable.m2m78,R.drawable.m2m79,R.drawable.m2m80
        )
    }

}