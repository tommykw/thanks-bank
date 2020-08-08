<#import "common/container.ftl" as b>

<@b.page>
    <div class="letter-detail-thank">
        <div class="letter-detail-thank-fromto">
            <img class="letter-detail-thank-fromto-image-you" src="${thank.userImage}" width="40" height="40"/>
            <img class="letter-detail-thank-fromto-image-target" src="${thank.targetUserImage}" width="40" height="40"/>
            <div class="letter-detail-thank-fromto-message">${thank.realName}さんから${thank.targetRealName}さんへ</div>
        </div>
        <div class="letter-detail-thank-message">${thank.body}</div>
    </div>

    <#if reactions?? && (reactions?size > 0)>
        <div class="letter-detail-reaction">
            <div class="letter-detail-reaction-title">集まったリアクション</div>
            <#list reactions as reaction>
                <img class="letter-detail-reaction-icon" src="${reaction.reactionName}" alt="${reaction.reactionName}" width="20" height="20"/>
            </#list>
        </div>
    </#if>

    <#if threads?? && (threads?size > 0)>
        <div class="letter-detail-tread">
            <#list threads as thread>
                <div class="letter-detail-tread-title">スレッド</div>
                <div class="letter-detail-tread-user-info">
                    <img class="letter-detail-tread-user-info-icon" src="http://wwwjp.kodak.com/JP/images/ja/digital/cameras/samplePicture/dc21203.jpg" width="40" height="40"/>
                    <div class="letter-detail-tread-user-info-name">たむたむ</div>
                </div>
                <div class="letter-detail-tread-message">こちらこそありがとう</div>
            </#list>
        </div>
    </#if>
</@b.page>