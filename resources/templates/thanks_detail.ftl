<#global page_title = "おてがみ詳細" />
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
            <p>${reactions?size}件</p>
        </div>
    </#if>

    <#if threads?? && (threads?size > 0)>
        <div class="letter-detail-tread">
            <div class="letter-detail-tread-title">スレッド</div>
            <#list threads as thread>
                <div class="letter-detail-tread-user-info">
                    <img class="letter-detail-tread-user-info-icon" src="${thread.userImage}" width="40" height="40"/>
                    <div class="letter-detail-tread-user-info-name">${thread.realName}</div>
                </div>
                <div class="letter-detail-tread-message">${thread.body}</div>
            </#list>
        </div>
    </#if>
</@b.page>