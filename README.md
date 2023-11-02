[![CICD](https://img.shields.io/badge/HarrierPanels-CD%2FPipeline-blue)](./)
[![EPAM](https://img.shields.io/badge/EPAM-Jenkins%20Task-orange)](./)
[![HitCount](https://hits.dwyl.com/HarrierPanels/my-java-project.svg?style=flat&show=unique)](http://hits.dwyl.com/HarrierPanels/my-java-project)
<br>


<div id="pipeline-box">
<h2>cd-pipeline - Stage View</h2>
<div class="table-box"><div class="table-viewPort">
<table class="jobsTable" style="max-width:70em">
  <colgroup>
    <col class="start-group">
      <col class="stage-group" style="max-width:10em;">
      <col class="stage-group" style="max-width:10em;">
      <col class="stage-group" style="max-width:10em;">
      <col class="stage-group" style="max-width:10em;">
      <col class="stage-group" style="max-width:10em;">
      <col class="stage-group" style="max-width:10em;">
      <col class="stage-group" style="max-width:10em;">
  </colgroup>
    <thead>
    <tr class="header">
        <th class="stage-start"></th>
        <th class="stage-header-name-0">Declarative: Checkout SCM</th>
        <th class="stage-header-name-1">Checkout</th>
        <th class="stage-header-name-2">Fetch Docker Hub Tags</th>
        <th class="stage-header-name-3">Select Environment</th>
        <th class="stage-header-name-4">Deploy to Environment</th>
        <th class="stage-header-name-5">Health Check</th>
        <th class="stage-header-name-6">Declarative: Post Actions</th>
    </tr>
    </thead>
    <tbody class="totals-box">
    <tr class="totals">
        <td class="stage-start"><div class="cell-color">
          Average stage times:<br>
          (Average <span style="text-decoration: underline;" title="builds that run all stages">full</span> run time: ~4min 16s)
        </div></td>
        <td class="stage-total-0">
          <div class="cell-color">
            <div class="duration">58s</div>
            <div class="stackedBarChart"><div class="stackedBarChart inner clearfix"><div class="bar highlight" style="width: 43.6347%;"></div><div class="bar posthighlight" style="width: 3.98664%;"></div><div class="bar posthighlight" style="width: 5.04529%;"></div><div class="bar posthighlight" style="width: 0.593912%;"></div><div class="bar posthighlight" style="width: 26.3927%;"></div><div class="bar posthighlight" style="width: 19.9087%;"></div><div class="bar posthighlight" style="width: 0.43801%;"></div><div class="clearfix"></div></div></div>
          </div>
        </td>
        <td class="stage-total-1">
          <div class="cell-color">
            <div class="duration">5s</div>
            <div class="stackedBarChart"><div class="stackedBarChart inner clearfix"><div class="bar prehighlight" style="width: 43.6347%;"></div><div class="bar highlight" style="width: 3.98664%;"></div><div class="bar posthighlight" style="width: 5.04529%;"></div><div class="bar posthighlight" style="width: 0.593912%;"></div><div class="bar posthighlight" style="width: 26.3927%;"></div><div class="bar posthighlight" style="width: 19.9087%;"></div><div class="bar posthighlight" style="width: 0.43801%;"></div><div class="clearfix"></div></div></div>
          </div>
        </td>
        <td class="stage-total-2">
          <div class="cell-color">
            <div class="duration">6s</div>
            <div class="stackedBarChart"><div class="stackedBarChart inner clearfix"><div class="bar prehighlight" style="width: 43.6347%;"></div><div class="bar prehighlight" style="width: 3.98664%;"></div><div class="bar highlight" style="width: 5.04529%;"></div><div class="bar posthighlight" style="width: 0.593912%;"></div><div class="bar posthighlight" style="width: 26.3927%;"></div><div class="bar posthighlight" style="width: 19.9087%;"></div><div class="bar posthighlight" style="width: 0.43801%;"></div><div class="clearfix"></div></div></div>
          </div>
        </td>
        <td class="stage-total-3">
          <div class="cell-color">
            <div class="duration">800ms</div>
            <div class="stackedBarChart"><div class="stackedBarChart inner clearfix"><div class="bar prehighlight" style="width: 43.6347%;"></div><div class="bar prehighlight" style="width: 3.98664%;"></div><div class="bar prehighlight" style="width: 5.04529%;"></div><div class="bar highlight" style="width: 0.593912%;"></div><div class="bar posthighlight" style="width: 26.3927%;"></div><div class="bar posthighlight" style="width: 19.9087%;"></div><div class="bar posthighlight" style="width: 0.43801%;"></div><div class="clearfix"></div></div></div>
          </div>
        </td>
        <td class="stage-total-4">
          <div class="cell-color">
            <div class="duration">35s</div>
            <div class="stackedBarChart"><div class="stackedBarChart inner clearfix"><div class="bar prehighlight" style="width: 43.6347%;"></div><div class="bar prehighlight" style="width: 3.98664%;"></div><div class="bar prehighlight" style="width: 5.04529%;"></div><div class="bar prehighlight" style="width: 0.593912%;"></div><div class="bar highlight" style="width: 26.3927%;"></div><div class="bar posthighlight" style="width: 19.9087%;"></div><div class="bar posthighlight" style="width: 0.43801%;"></div><div class="clearfix"></div></div></div>
          </div>
        </td>
        <td class="stage-total-5">
          <div class="cell-color">
            <div class="duration">26s</div>
            <div class="stackedBarChart"><div class="stackedBarChart inner clearfix"><div class="bar prehighlight" style="width: 43.6347%;"></div><div class="bar prehighlight" style="width: 3.98664%;"></div><div class="bar prehighlight" style="width: 5.04529%;"></div><div class="bar prehighlight" style="width: 0.593912%;"></div><div class="bar prehighlight" style="width: 26.3927%;"></div><div class="bar highlight" style="width: 19.9087%;"></div><div class="bar posthighlight" style="width: 0.43801%;"></div><div class="clearfix"></div></div></div>
          </div>
        </td>
        <td class="stage-total-6">
          <div class="cell-color">
            <div class="duration">590ms</div>
            <div class="stackedBarChart"><div class="stackedBarChart inner clearfix"><div class="bar prehighlight" style="width: 43.6347%;"></div><div class="bar prehighlight" style="width: 3.98664%;"></div><div class="bar prehighlight" style="width: 5.04529%;"></div><div class="bar prehighlight" style="width: 0.593912%;"></div><div class="bar prehighlight" style="width: 26.3927%;"></div><div class="bar prehighlight" style="width: 19.9087%;"></div><div class="bar highlight" style="width: 0.43801%;"></div><div class="clearfix"></div></div></div>
          </div>
        </td>
    </tr>
  </tbody>
    <tbody class="tobsTable-body">
    <tr class="job SUCCESS" data-runid="142">
        <td class="stage-start">
          <div class="cell-color">
            <div class="cell-box">
              <div class="jobName"><span class="badge"><a href="142">#142</a></span></div>
              <div class="stage-start-box">
                <div class="stage-start-time">
                  <div class="date">Nov 02</div>
                  <div class="time">21:14</div>
                </div>
                                <div class="changeset-box no-changes">No Changes</div>
                <div class="stage-end-icons extension-dock">
                </div>
              </div>

              <div class="clearfix"></div>

            </div>
          </div>
        </td>
        <td class="stage-cell stage-cell-0 SUCCESS" data-stageid="6">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.0229135271508152em; opacity: 0.5229135271508152;">5s</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/142/execution/node/6/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
        <td class="stage-cell stage-cell-1 SUCCESS" data-stageid="15">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.015818926835876em; opacity: 0.5158189268358759;">3s</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/142/execution/node/15/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
        <td class="stage-cell stage-cell-2 SUCCESS" data-stageid="20">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.0179015899354882em; opacity: 0.5179015899354882;">4s</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/142/execution/node/20/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                    <div class="pause-duration">
                        <span title="paused for 6s">(paused for 6s)</span>
                    </div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
        <td class="stage-cell stage-cell-3 SUCCESS" data-stageid="30">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.0031155285392575em; opacity: 0.5031155285392575;">736ms</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/142/execution/node/30/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                    <div class="pause-duration">
                        <span title="paused for 15s">(paused for 15s)</span>
                    </div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
        <td class="stage-cell stage-cell-4 SUCCESS" data-stageid="39">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.2930290048934117em; opacity: 0.7930290048934117;">1min 9s</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/142/execution/node/39/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
        <td class="stage-cell stage-cell-5 SUCCESS" data-stageid="66">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.0321119558407694em; opacity: 0.5321119558407694;">7s</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/142/execution/node/66/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
        <td class="stage-cell stage-cell-6 SUCCESS" data-stageid="81">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.0018583111803452em; opacity: 0.5018583111803452;">439ms</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/142/execution/node/81/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
    </tr>
    <tr class="job SUCCESS" data-runid="141">
        <td class="stage-start">
          <div class="cell-color">
            <div class="cell-box">
              <div class="jobName"><span class="badge"><a href="141">#141</a></span></div>
              <div class="stage-start-box">
                <div class="stage-start-time">
                  <div class="date">Nov 02</div>
                  <div class="time">21:02</div>
                </div>
                <div class="changeset-box cbwf-widget cbwf-controller-applied run-changesets" cbwf-controller="run-changesets" objecturl="/job/cd-pipeline/141/wfapi/changesets"><div><div class="run-changesets">
    <div class="run-changeset">
        <strong class="num">1</strong>
        <span class="changes commitLabel">commit</span>
    </div>
</div></div></div>
                <div class="stage-end-icons extension-dock">
                </div>
              </div>

              <div class="clearfix"></div>

            </div>
          </div>
        </td>
        <td class="stage-cell stage-cell-0 SUCCESS" data-stageid="6">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.1077688694382186em; opacity: 0.6077688694382186;">1min 25s</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/141/execution/node/6/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
        <td class="stage-cell stage-cell-1 SUCCESS" data-stageid="15">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.0078103087990418em; opacity: 0.5078103087990418;">6s</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/141/execution/node/15/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
        <td class="stage-cell stage-cell-2 SUCCESS" data-stageid="20">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.0100995808054902em; opacity: 0.5100995808054902;">7s</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/141/execution/node/20/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                    <div class="pause-duration">
                        <span title="paused for 27s">(paused for 27s)</span>
                    </div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
        <td class="stage-cell stage-cell-3 SUCCESS" data-stageid="30">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.0007795148057277em; opacity: 0.5007795148057277;">617ms</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/141/execution/node/30/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                    <div class="pause-duration">
                        <span title="paused for 6s">(paused for 6s)</span>
                    </div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
        <td class="stage-cell stage-cell-4 SUCCESS" data-stageid="39">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.0293360353144212em; opacity: 0.5293360353144212;">23s</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/141/execution/node/39/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
        <td class="stage-cell stage-cell-5 SUCCESS" data-stageid="55">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.090763570758972em; opacity: 0.590763570758972;">1min 11s</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/141/execution/node/55/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
        <td class="stage-cell stage-cell-6 SUCCESS" data-stageid="70">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.0005634742355827em; opacity: 0.5005634742355827;">446ms</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/141/execution/node/70/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
    </tr>
    <tr class="job FAILED" data-runid="140">
        <td class="stage-start">
          <div class="cell-color">
            <div class="cell-box">
              <div class="jobName"><span class="badge"><a href="140">#140</a></span></div>
              <div class="stage-start-box">
                <div class="stage-start-time">
                  <div class="date">Nov 02</div>
                  <div class="time">20:13</div>
                </div>
                <div class="changeset-box cbwf-widget cbwf-controller-applied run-changesets" cbwf-controller="run-changesets" objecturl="/job/cd-pipeline/140/wfapi/changesets"><div><div class="run-changesets">
    <div class="run-changeset">
        <strong class="num">5</strong>
        <span class="changes commitLabel">commits</span>
    </div>
</div></div></div>
                <div class="stage-end-icons extension-dock">
                </div>
              </div>

              <div class="clearfix"></div>

            </div>
          </div>
        </td>
        <td class="stage-cell stage-cell-0 SUCCESS" data-stageid="6">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.1375312045185106em; opacity: 0.6375312045185106;">1min 25s</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/140/execution/node/6/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
        <td class="stage-cell stage-cell-1 SUCCESS" data-stageid="15">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.0099468920781487em; opacity: 0.5099468920781487;">6s</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/140/execution/node/15/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
        <td class="stage-cell stage-cell-2 SUCCESS" data-stageid="20">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.013116339440905em; opacity: 0.5131163394409051;">8s</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/140/execution/node/20/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                    <div class="pause-duration">
                        <span title="paused for 43s">(paused for 43s)</span>
                    </div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
        <td class="stage-cell stage-cell-3 SUCCESS" data-stageid="30">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.0016819114996482em; opacity: 0.5016819114996482;">1s</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/140/execution/node/30/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                    <div class="pause-duration">
                        <span title="paused for 5s">(paused for 5s)</span>
                    </div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
        <td class="stage-cell stage-cell-4 FAILED" data-stageid="39">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.0228270892168108em; opacity: 0.5228270892168108;">14s</span>
                </div>
                    <div cbwf-controller="stage-failed-popover" descurl="/job/cd-pipeline/140/execution/node/39/wfapi/describe" objecturl="/job/cd-pipeline/140/execution/node/39/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" class="cbwf-widget cbwf-controller-applied stage-failed-popover"></div>
                    <div class="status">failed</div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
        <td class="stage-cell stage-cell-5 FAILED" data-stageid="53">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.0016449640646035em; opacity: 0.5016449640646035;">1s</span>
                </div>
                    <div cbwf-controller="stage-failed-popover" descurl="/job/cd-pipeline/140/execution/node/53/wfapi/describe" objecturl="/job/cd-pipeline/140/execution/node/53/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" class="cbwf-widget cbwf-controller-applied stage-failed-popover"></div>
                    <div class="status">failed</div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
        <td class="stage-cell stage-cell-6 SUCCESS" data-stageid="57">
          <div class="cell-color">
            <div class="stage-wrapper">
                <div class="duration">
                    <span style="font-size: 1.0014216730441152em; opacity: 0.5014216730441152;">885ms</span>
                </div>
                    <div cbwf-controller="stage-actions-popover" descurl="/job/cd-pipeline/140/execution/node/57/wfapi/describe" notif="stage-actions-popover,stage-failed-popover" caption="Success" class="cbwf-widget cbwf-controller-applied stage-actions-popover"></div>
                <div class="extension-dock"></div>
            </div>
          </div>
    </td>
    </tr>
  </tbody>

</table>
</div>
</div></div>
