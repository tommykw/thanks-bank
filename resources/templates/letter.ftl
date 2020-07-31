<#import "common/milligram.ftl" as b>

<@b.page>
    <div class="cards">
        <a href="/letter/detail">
            <div class="card" onclick="">

                <div class="card-to-user">
                    <img class="card_user_image" src="http://wwwjp.kodak.com/JP/images/ja/digital/cameras/samplePicture/dc21203.jpg" width="40" height="40"/>
                    <div class="card-to-user-name">ほげこさんへ</div>
                </div>

                <div class="card_content">
                    <p>AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA</p>
                </div>

                <div class="card_reaction">
                    <img class="card_user_image" src="http://wwwjp.kodak.com/JP/images/ja/digital/cameras/samplePicture/dc21203.jpg" width="20" height="20"/>
                    <div class="card_reaction_count">スレッド10件</div>
                </div>

                <div class="card_info">
                    <img class="card-from-image" src="http://wwwjp.kodak.com/JP/images/ja/digital/cameras/samplePicture/dc21203.jpg" width="30" height="30"/>
                    <div class="card-from-user-name">ほげこさんより</div>
                </div>
            </div>
        </a>

        <div class="card">
            <div class="card-to-user">
                <img class="card_user_image" src="http://wwwjp.kodak.com/JP/images/ja/digital/cameras/samplePicture/dc21203.jpg" width="40" height="40"/>
                <div class="card-to-user-name">ほげこさんへ</div>
            </div>

            <div class="card_content">
                <p>AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA</p>
            </div>

            <div class="card_reaction">
                <img class="card_user_image" src="http://wwwjp.kodak.com/JP/images/ja/digital/cameras/samplePicture/dc21203.jpg" width="20" height="20"/>
                <div class="card_reaction_count">スレッド10件</div>
            </div>

            <div class="card_info">
                <img class="card-from-image" src="http://wwwjp.kodak.com/JP/images/ja/digital/cameras/samplePicture/dc21203.jpg" width="30" height="30"/>
                <div class="card-from-user-name">ほげこさんより</div>
            </div>
        </div>

        <div class="card">
            <div class="card-to-user">
                <img class="card_user_image" src="http://wwwjp.kodak.com/JP/images/ja/digital/cameras/samplePicture/dc21203.jpg" width="40" height="40"/>
                <div class="card-to-user-name">ほげこさんへ</div>
            </div>

            <div class="card_content">
                <p>AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA</p>
            </div>

            <div class="card_reaction">
                <img class="card_user_image" src="http://wwwjp.kodak.com/JP/images/ja/digital/cameras/samplePicture/dc21203.jpg" width="20" height="20"/>
                <div class="card_reaction_count">スレッド10件</div>
            </div>

            <div class="card_info">
                <img class="card-from-image" src="http://wwwjp.kodak.com/JP/images/ja/digital/cameras/samplePicture/dc21203.jpg" width="30" height="30"/>
                <div class="card-from-user-name">ほげこさんより</div>
            </div>
        </div>

        <div class="card">
            <div class="card-to-user">
                <img class="card_user_image" src="http://wwwjp.kodak.com/JP/images/ja/digital/cameras/samplePicture/dc21203.jpg" width="40" height="40"/>
                <div class="card-to-user-name">ほげこさんへ</div>
            </div>

            <div class="card_content">
                <p>AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA</p>
            </div>

            <div class="card_reaction">
                <img class="card_user_image" src="http://wwwjp.kodak.com/JP/images/ja/digital/cameras/samplePicture/dc21203.jpg" width="20" height="20"/>
                <div class="card_reaction_count">スレッド10件</div>
            </div>

            <div class="card_info">
                <img class="card-from-image" src="http://wwwjp.kodak.com/JP/images/ja/digital/cameras/samplePicture/dc21203.jpg" width="30" height="30"/>
                <div class="card-from-user-name">ほげこさんより</div>
            </div>
        </div>

    </div>
<#--    <#if thanks?? && (thanks?size > 0)>-->
<#--        <#list thanks as thank>-->
<#--            <div class="thank-card">-->
<#--                <div class="user-container">-->
<#--                    <img class="user-image" src="http://wwwjp.kodak.com/JP/images/ja/digital/cameras/samplePicture/dc21203.jpg" height="40" width="40"/>-->
<#--                    <span class="user-name">${thank.realName}名前さんへ</span>-->
<#--                </div>-->
<#--                <div>${thank.body}</div>-->
<#--                <div>リアクションとリアクション数</div>-->
<#--                <div class="target-user-container">-->
<#--                    <img class="target-user-image"  src="http://wwwjp.kodak.com/JP/images/ja/digital/cameras/samplePicture/dc21203.jpg" height="40" width="40"/>-->
<#--                    <span class="target-user-name">XXXより</span>-->
<#--                </div>-->
<#--            </div>-->
<#--        </#list>-->
<#--    </#if>-->
</@b.page>