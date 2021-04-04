<#global page_title = "サンクス詳細" />
<#import "common/container.ftl" as container>

<@container.page>
    <div class="thanks-detail">
        <div class="thanks-detail-fromto">
            <img class="thanks-detail-fromto-image-you" src="${thank.userImage}" width="40" height="40"/>
            <img class="thanks-detail-fromto-image-target" src="${thank.targetUserImage}" width="40" height="40"/>
            <p class="thanks-detail-fromto-message">${thank.realName}さんから${thank.targetRealName}さんへ</p>
        </div>
        <div class="thanks-detail-message">${thank.body}</div>
    </div>

    <#if reactions?? && (reactions?size > 0)>
        <div class="thanks-detail-reaction">
            <p class="thanks-detail-reaction-title">集まったリアクション</p>
            <p>${reactions?size}件</p>
        </div>
    </#if>

    <#if threads?? && (threads?size > 0)>
        <div class="thanks-detail-tread">
            <p class="thanks-detail-tread-title">スレッド</p>
            <#list threads as thread>
                <div class="thanks-detail-tread-user-info">
                    <img class="thanks-detail-tread-user-info-icon" src="${thread.userImage}" width="40" height="40"/>
                    <p class="thanks-detail-tread-user-info-name">${thread.realName}</p>
                </div>
                <div class="thanks-detail-tread-message">${thread.body}</div>
            </#list>
        </div>
    </#if>
</@container.page>