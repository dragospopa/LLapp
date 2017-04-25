package georgia.languagelandscape.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import georgia.languagelandscape.R;

/**
 * A generic Fragment that display terms and services text
 *
 * The text is taken as html text from
 * <a href="http://languagelandscape.org/terms/">Language Landscape website</a>
 * and is formatted by Html.fromHtml() method.
 */
public class TermsFragment extends Fragment {

    private Context context;

    private static final String TERMS_AND_SERVICES =
            "<p><strong>1. Your relationship with Language Landscape</strong></p>" +
                    "<p>1.1 Your use of the Language Landscape website (the “Website”) and any Language Landscape software, including the Language Landscape embeddable map (the “Language Landscape Map”) and embeddable audio player (the “Language Landscape Player”) provided to you on or from or through the Website by Language Landscape (collectively the “Service”) is subject to the terms of a legal agreement between you and Language Landscape. “Language Landscape” means Language Landscape, whose principal place of business is at 5 Dorchester Court, Colney Hatch Lane, London N10 1BU, United Kingdom.</p>" +
                    "<p>1.2 Your legal agreement with Language Landscape is made up of (A) the terms and conditions set out in this document, and (B) <a title=\"Community Guidelines\" href=\"/community-guidelines/\">Language Landscape’s Community Guidelines</a> (collectively called the “Terms”).</p>" +
                    "<p>1.3 The Terms form a legally binding agreement between you and Language Landscape in relation to your use of the Service. It is important that you take the time to read them carefully.</p>" +
                    "<p>1.4 The Terms apply to all users of the Service, including users who are also contributors of Content, on the Service. “Content” includes the text, software, scripts, graphics, photos, sounds, music, audios, audiovisual combinations, interactive features and other materials you may view on, access through or contribute to the Service.</p>" +
                    "<p><strong>2. Accepting the Terms</strong></p>" +
                    "<p>2.1 In order to use the Service, you must firstly agree to the Terms. You may not use the Service if you do not accept the Terms.</p>" +
                    "<p>2.2 You can accept the Terms by simply using the Service. You understand and agree that Language Landscape will treat your use of the Service as acceptance of the Terms from that point onwards.</p>" +
                    "<p>2.3 You may not use the Service and may not accept the Terms if (a) you are not of legal age to form a binding contract with Language Landscape, or (b) you are a person who is either barred or otherwise legally prohibited from receiving or using the Service under the laws of the country in which you are resident or from which you access or use the Service.</p>" +
                    "<p>2.4 You should print off or save a local copy of the Terms for your records.</p>" +
                    "<p><strong>3. Changes to the Terms</strong></p>" +
                    "<p>Language Landscape reserves the right to make changes the Terms from time to time, for example to address changes to the law or regulatory changes or changes to functionality offered through the Service. Therefore you must look at the Terms regularly to check for such changes. The modified version of the Terms (the “Modified Terms”) will be posted at <a title=\"Terms of Service\" href=\"/terms/\">languagelandscape.org/terms</a>&nbsp;or made available within the Service (for any modified additional terms). If you do not agree to the Modified Terms you must stop using the Service. Your continued use of the Service after the date the Modified Terms are posted will constitute your acceptance of the Modified Terms.</p>" +
                    "<p><strong>4. Language Landscape accounts</strong></p>" +
                    "<p>4.1 In order to access some features of the Website or other elements of the Service, you will have to create a Language Landscape account. When creating your account, you must provide accurate and complete information. It is important to keep your Language Landscape account password secure and confidential.</p>\n" +
                    "<p>4.2 You must notify Language Landscape immediately of any breach of security or unauthorised use of your Language Landscape account that you become aware of.</p>\n" +
                    "<p>4.3 You agree that you will be solely responsible (to Language Landscape, and to others) for all activity that occurs under your Language Landscape account.</p>\n" +
                    "<p><strong>5. General restrictions on use</strong></p>" +
                    "<p>5.1 Language Landscape hereby grants you permission to access and use the Service, subject to the following express conditions, and you agree that your failure to adhere to any of these conditions shall constitute a breach of these Terms on your part:</p>\n" +
                    "<ol>" +
                    "<li>You agree not to distribute any part of or parts of the Website or the Service, including but not limited to any Content, in any medium without Language Landscape’s prior written authorisation, unless Language Landscape makes available the means for such distribution through functionality offered by the Service (such as the Language Landscape Map and the Language Landscape Player);</li>\n" +
                    "<li>You agree not to alter or modify any part of the Website or any of the Service (including but not limited to the Language Landscape Map and the Language Landscape Player);</li>\n" +
                    "<li>You agree not to access Content through any technology or means other than the audio/video playback pages of the Website itself, the Language Landscape Map and the Language Landscape Player, or such other means as Language Landscape may explicitly designate for this purpose;</li>\n" +
                    "<li>You agree not to use the Service (including the Language Landscape Map and the Language Landscape Player) for any of the following commercial uses unless you obtain Language Landscape’s prior written approval:\n" +
                    "<ol>" +
                    "<li>The sale of access to the Service</li>" +
                    "<li>The sale of advertising, sponsorships or promotions placed on or within the Service, or Content;</li>" +
                    "<li>The sale of advertising, sponsorships or promotions on any page of an ad-enabled blog or website containing Content delivered via the Service unless other material not obtained from Language Landscape appears on the same page and is of sufficient value to be the basis for such sales.</li>\n" +
                    "</ol>" +
                    "</li>" +
                    "<li>Prohibited commercial uses shall not include (i) uploading an original audio clip to Language Landscape or adding an original video clip uploaded to YouTube, (ii) maintaining an original project on the Website in order to promote a business or artistic enterprise, and (iii) showing Language Landscape audio clips or YouTube video clips through the Language Landscape Map and the Language Landscape Player or otherwise on an ad-enabled blog or website.</li>\n" +
                    "<li>If you use the Language Landscape Map and the Language Landscape Player on your website you may not modify, build upon or block any portion or functionality of the Language Landscape Map and the Language Landscape Player including but not limited to links back to the Website;</li>\n" +
                    "<li>You agree not to use or launch any automated system (including, without limitation, any robot, spider or offline reader) that accesses the Service in a manner that sends more request messages to the Language Landscape servers in a given period of time than a human can reasonably produce in the same period by using a publicly available, standard (i.e. not modified) web browser;</li>\n" +
                    "<li>You agree not to collect or harvest any personal data of any user of the Website or any Service (and agree that this includes Language Landscape account names);</li>\n" +
                    "<li>You agree not to use the Website or the Services for the solicitation of business in the course of trade or in connection with a commercial enterprise;</li>\n" +
                    "<li>You agree not to solicit, for commercial purposes, any users of the Website with respect to their Content; and</li>" +
                    "<li>You agree not to access Content for any reason other than your personal, non-commercial use solely as intended through and permitted by the normal functionality of the Service, and solely for Streaming. “Streaming” means a contemporaneous digital transmission of the material by Language Landscape via the Internet to a user operated Internet enabled device in such a manner that the data is intended for real-time viewing/listening and not intended to be downloaded (either permanently or temporarily), copied, stored, or redistributed by the user.</li>\n" +
                    "<li>You shall not copy, reproduce, distribute, transmit, broadcast, display, sell, license, or otherwise exploit any Content for any other purposes without the prior written consent of Language Landscape or the Content owners.</li>\n" +
                    "</ol>" +
                    "<p>5.2 You agree that you will comply with all of the other provisions of the Terms and the Language Landscape Community Guidelines at all times during your use of the Service.</p>\n" +
                    "<p>5.3 Language Landscape grants the operators of public search engines permission to use spiders to copy materials from the site for the sole purpose of creating publicly available searchable indices of the materials, but not caches or archives of such materials. Language Landscape reserves the right to revoke these exceptions either generally or in specific cases.</p>\n" +
                    "<p>5.4 Language Landscape is constantly innovating in order to provide the best possible experience for its users. You acknowledge and agree that the form and nature of the Service which Language Landscape provides may change from time to time without prior notice to you.</p>\n" +
                    "<p>5.5 As part of this continuing innovation, you acknowledge and agree that Language Landscape may stop (permanently or temporarily) providing the Service (or any features within the Service) to you or to users generally at Language Landscape’s sole discretion, without prior notice to you. You may stop using the Service at any time. You do not need to specifically inform Language Landscape when you stop using the Service.</p>\n" +
                    "<p>5.6 You agree that you are solely responsible for (and that Language Landscape has no responsibility to you or to any third party for) any breach of your obligations under the Terms and for the consequences (including any loss or damage which Language Landscape may suffer) of any such breach.</p>\n" +
                    "<p><strong>6. Copyright policy</strong></p>\n" +
                    "<p>6.1 Language Landscape operates a clear copyright policy in relation to any Content that is alleged to infringe the copyright of a third party. Details of that policy can be found here: <a title=\"Copyright Notice\" href=\"/copyright-notice/\">languagelandscape.org/copyright-notice</a></p>\n" +
                    "<p>6.2 As part of Language Landscape’s copyright policy, Language Landscape will terminate user access to the Service if a user has been determined to be a repeat infringer. A repeat infringer is a user who has been notified of infringing activity more than twice.</p>\n" +
                    "<p><strong>7. Content</strong></p>\n" +
                    "<p>7.1 As a Language Landscape account holder you may submit Content. You understand that whether or not Content is published, Language Landscape does not guarantee any confidentiality with respect to Content.</p>\n" +
                    "<p>7.2 You retain all of your ownership rights in your Content, but you are required to grant limited licence rights to Language Landscape and other users of the Service. These are described in paragraph 8 of these Terms (Rights you licence).</p>\n" +
                    "<p>7.3 You understand and agree that you are solely responsible for your own Content and the consequences of posting or publishing it. Language Landscape does not endorse any Content or any opinion, recommendation, or advice expressed therein, and Language Landscape expressly disclaims any and all liability in connection with Content.</p>\n" +
                    "<p>7.4 You represent and warrant that you have (and will continue to have during your use of the Service) all necessary licenses, rights, consents, and permissions which are required to enable Language Landscape to use your Content for the purposes of the provision of the Service, and otherwise to use your Content in the manner contemplated by the Service and these Terms.</p>\n" +
                    "<p>7.5 You agree that your conduct on the site will comply with (and you agree that the content of all of your Content shall comply with) the Language Landscape Community Guidelines, found at <a title=\"Community Guidelines\" href=\"/community-guidelines/\">languagelandscape.org/community-guidelines</a>.</p>\n" +
                    "<p>7.6 You agree that you will not post or upload any Content which contains material which it is unlawful for you to possess in the country in which you are resident, or which it would be unlawful for Language Landscape to use or possess in connection with the provision of the Service.</p>\n" +
                    "<p>7.7 You agree that Content you submit to the Service will not contain any third party copyright material, or material that is subject to other third party proprietary rights (including rights of privacy or rights of publicity), unless you have a formal licence or permission from the rightful owner, or are otherwise legally entitled, to post the material in question and to grant Language Landscape the licence referred to in paragraph 8.1 below.</p>\n" +
                    "<p>7.8 On becoming aware of any potential violation of these Terms, Language Landscape reserves the right (but shall have no obligation) to decide whether Content complies with the content requirements set out in these Terms and may remove such Content and/or terminate a User’s access for uploading Content which is in violation of these Terms at any time, without prior notice and at its sole discretion.</p>\n" +
                    "<p>7.9 You further understand and acknowledge that in using the Service, you may be exposed to Content that is factually inaccurate, offensive, indecent, or otherwise objectionable to you. You agree to waive, and hereby do waive, any legal or equitable rights or remedies you have or may have against Language Landscape with respect to any such Content.</p>\n" +
                    "<p><strong>8. Rights you licence</strong></p>\n" +
                    "<p>8.1 When you upload Content to Language Landscape, you grant:</p>\n" +
                    "<ol>\n" +
                    "<li>to Language Landscape, a worldwide, non-exclusive, royalty-free, transferable licence (with right to sub-licence) to use, reproduce, distribute, prepare derivative works of, display, and perform that Content in connection with the provision of the Service and otherwise in connection with the provision of the Service and Language Landscape’s business, including without limitation for promoting and redistributing part or all of the Service (and derivative works thereof) in any media formats and through any media projects;</li>\n" +
                    "<li>to each user of the Service, a worldwide, non-exclusive, royalty-free licence to access your Content through the Service, and to use, reproduce, distribute, prepare derivative works of, display and perform such Content to the extent permitted by the functionality of the Service and under these Terms.</li>\n" +
                    "</ol>\n" +
                    "<p>8.2 The above licenses granted by you in Content terminate when you remove or delete your Content from the Website. The above licenses granted by you in textual comments you submit as Content are perpetual and irrevocable, but are otherwise without prejudice to your ownerships rights, which are retained by you as set out in paragraph 7.2 above.</p>\n" +
                    "<p><strong>9. Language Landscape content on the Website</strong></p>\n" +
                    "<p>9.1 With the exception of Content submitted to the Service by you, all other Content on the Service is either owned by or licensed to Language Landscape, and is subject to copyright, trade mark rights, and other intellectual property rights of Language Landscape. Any third party trade or service marks present on Content not uploaded or posted by you are trade or service marks of their respective owners. Such Content may not be downloaded, copied, reproduced, distributed, transmitted, broadcast, displayed, sold, licensed, or otherwise exploited for any other purpose whatsoever without the prior written consent of Language Landscape. Language Landscape reserve all rights not expressly granted in and to their Content.</p>\n" +
                    "<p><strong>10. Links from Language Landscape</strong></p>\n" +
                    "<p>10.1 The Service may include hyperlinks to other web sites that are not owned or controlled by Language Landscape. Language Landscape has no control over, and assumes no responsibility for, the content, privacy policies, or practices of any third party websites.</p>\n" +
                    "<p>10.2 You acknowledge and agree that Language Landscape is not responsible for the availability of any such external sites or resources, and does not endorse any advertising, products or other materials on or available from such web sites or resources.</p>\n" +
                    "<p>10.3 You acknowledge and agree that Language Landscape is not liable for any loss or damage which may be incurred by you as a result of the availability of those external sites or resources, or as a result of any reliance placed by you on the completeness, accuracy or existence of any advertising, products or other materials on, or available from, such web sites or resources.</p>\n" +
                    "<p><strong>11. Ending your relationship with Language Landscape</strong></p>\n" +
                    "<p>11.1 The Terms will continue to apply until terminated by either you or Language Landscape as set out below.</p>\n" +
                    "<p>11.2 If you want to terminate your legal agreement with Language Landscape, you may do so by (a) notifying Language Landscape at any time and (b) closing your Language Landscape account. Your notice should be sent, in writing, to Language Landscape’s address which is set out at the beginning of these Terms.</p>\n" +
                    "<p>11.3 Language Landscape may at any time terminate its legal agreement with you if:</p>\n" +
                    "<ol>\n" +
                    "<li>you have breached any provision of the Terms (or have acted in manner which clearly shows that you do not intend to, or are unable to comply with the provisions of the Terms); or</li>\n" +
                    "<li>Language Landscape is required to do so by law (for example, where the provision of the Service to you is, or becomes, unlawful); or</li>\n" +
                    "</ol>\n" +
                    "<p>11.4 Language Landscape may terminate its legal agreement with you if:</p>\n" +
                    "<ol>\n" +
                    "<li>Language Landscape is transitioning to no longer providing the Service to users in the country in which you are resident or from which you use the Service; or</li>\n" +
                    "<li>the provision of the Service to you by Language Landscape is, in Language Landscape’s opinion, no longer commercially viable</li>\n" +
                    "</ol>\n" +
                    "<p>and in the case of each of 1 and 2 of this clause 11.4 shall, where possible, give reasonable notice of such termination.</p>\n" +
                    "<p>11.5 When these Terms come to an end, all of the legal rights, obligations and liabilities that you and Language Landscape have benefited from, been subject to (or which have accrued over time whilst the Terms have been in force) or which are expressed to continue indefinitely, shall be unaffected by this cessation, and the provisions of paragraph 14.6 shall continue to apply to such rights, obligations and liabilities indefinitely.</p>\n" +
                    "<p><strong>12. Exclusion of Warranties</strong></p>\n" +
                    "<p>12.1 Nothing in the Terms shall affect any statutory rights that you are always entitled to as a consumer and that you cannot contractually agree to alter or waive.</p>\n" +
                    "<p>12.2 The Service is provided “as is” and Language Landscape makes no warranty or representation to you with respect to them.</p>\n" +
                    "<p>12.3 In particular Language Landscape does not represent or warrant to you that:</p>\n" +
                    "<ol>\n" +
                    "<li>your use of the Service will meet your requirements,</li>\n" +
                    "<li>your use of the Service will be uninterrupted, timely, secure or free from error,</li>\n" +
                    "<li>any information obtained by you as a result of your use of the Service will be accurate or reliable, and</li>\n" +
                    "<li>that defects in the operation or functionality of any software provided to you as part of the Service will be corrected.</li>\n" +
                    "</ol>\n" +
                    "<p>12.4 No conditions, warranties or other terms (including any implied terms as to satisfactory quality, fitness for purpose or conformance with description) apply to the Service except to the extent that they are expressly set out in the Terms.</p>\n" +
                    "<p><strong>13. Limitation of Liability</strong></p>\n" +
                    "<p>13.1 Nothing in these Terms shall exclude or limit Language Landscape’s liability for losses which may not be lawfully excluded or limited by applicable law.</p>\n" +
                    "<p>13.2 Subject to the overall provision in paragraph 13.1 above Language Landscape shall not be liable to you for:</p>\n" +
                    "<ol>\n" +
                    "<li>any indirect or consequential losses which may be incurred by you. This shall include; (i) any loss of profit (whether incurred directly or indirectly); (ii) any loss of goodwill or business reputation; (iii) any loss of opportunity; or (iv) any loss of data suffered by you;</li>\n" +
                    "<li>any loss or damage which may be incurred by you as a result of:\n" +
                    "<ol>\n" +
                    "<li>any changes which Language Landscape may make to the Service, or for any permanent or temporary cessation in the provision of the Service (or any features within the Service);</li>\n" +
                    "<li>the deletion of, corruption of, or failure to store, any Content and other communications data maintained or transmitted by or through your use of the Service;</li>\n" +
                    "<li>your failure to provide Language Landscape with accurate account information;</li>\n" +
                    "<li>your failure to keep your password or Language Landscape account details secure and confidential.</li>\n" +
                    "</ol>\n" +
                    "</li>\n" +
                    "</ol>\n" +
                    "<p>13.3 The limitations on Language Landscape’s liability to you in paragraph 13.2 above shall apply whether or not Language Landscape has been advised of or should have been aware of the possibility of any such losses arising.</p>\n" +
                    "<p><strong>14. General legal terms</strong></p>\n" +
                    "<p>14.1 The Terms constitute the whole legal agreement between you and Language Landscape and govern your use of the Service.</p>\n" +
                    "<p>14.2 You agree that Language Landscape may provide you with notices, including those regarding changes to the Terms, by email or postings on the Service.</p>\n" +
                    "<p>14.3 You agree that if Language Landscape does not exercise or enforce any legal right or remedy which is contained in the Terms (or which Language Landscape has the benefit of under any applicable law), this will not be taken to be a formal waiver of Language Landscape’s rights and that those rights or remedies will still be available to Language Landscape.</p>\n" +
                    "<p>14.4 If any court of law, having the jurisdiction to decide on this matter, rules that any provision of these Terms is invalid, then that provision will be removed from the Terms without affecting the rest of the Terms. The remaining provisions of the Terms will continue to be valid and enforceable.</p>\n" +
                    "<p>14.5 The Terms, and your relationship with Language Landscape under the Terms, shall be governed by English law. You and Language Landscape agree to submit to the exclusive jurisdiction of the courts of England to resolve any legal matter arising from the Terms. Notwithstanding this, you agree that Language Landscape shall still be allowed to apply for injunctive remedies (or other equivalent types of urgent legal remedy) in any jurisdiction.</p>\n" +
                    "<h4>Dated: 9 January 2014</h4>\n";

    public TermsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms, container, false);

        TextView terms = (TextView) view.findViewById(R.id.terms);
        terms.setText(Html.fromHtml(TERMS_AND_SERVICES));
        terms.setMovementMethod(new ScrollingMovementMethod());

        return view;
    }
}
