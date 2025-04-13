import QtQuick 2.6
import Sailfish.Silica 1.0
import "../tabview" as Tabs

Tabs.TabItem {
    id: root

    property var portfolioInfo: ({})
    property bool isLoading: true
    property bool isError: false

    ListModel {
        id: listStocks
    }

    function getPortfolio() {
        isLoading = true
        isError = false
        listStocks.clear()

        libKMPShared.run(
            "composeApp.ru.alex0d.investapp.aurora.portfolioRepository.getPortfolio()",
            function(response) {
                portfolioInfo = response
                for (var i = 0; i < response.stocks.array_1.length; i++) {
                    listStocks.append(response.stocks.array_1[i])
                }
                isLoading = false
            },
            function(error) {
                console.log(error)
                isLoading = false
                isError = true
            }
        )
    }

    SilicaFlickable {
        anchors.fill: parent
        contentHeight: column.height + Theme.paddingLarge

        PullDownMenu {
            MenuItem {
                text: qsTr("Reload")
                onClicked: getPortfolio()
            }
        }

        Column {
            id: column
            width: parent.width
            spacing: Theme.paddingMedium

            PageHeader {
                title: qsTr("Portfolio")
            }

            Item {
                width: parent.width
                height: Theme.itemSizeLarge
                visible: !isLoading && !isError && listStocks.count > 0

                Rectangle {
                    anchors.fill: parent
                    color: Theme.highlightBackgroundColor
                    opacity: 0.1
                }

                Column {
                    anchors.centerIn: parent
                    width: parent.width - Theme.horizontalPageMargin * 2

                    Label {
                        width: parent.width
                        text: portfolioInfo.totalValue ?
                              portfolioInfo.totalValue.toLocaleString(Qt.locale(), 'f', 2) + " RUB" :
                              ""
                        font.pixelSize: Theme.fontSizeExtraLarge
                        color: Theme.highlightColor
                    }

                    Label {
                        width: parent.width
                        text: portfolioInfo.totalProfit ?
                              portfolioInfo.totalProfit.toLocaleString(Qt.locale(), 'f', 2) + " RUB (" +
                              portfolioInfo.totalProfitPercent.toLocaleString(Qt.locale(), 'f', 2) + "%)" :
                              ""
                        font.pixelSize: Theme.fontSizeSmall
                        color: portfolioInfo.totalProfit > 0 ? "green" : "red"
                    }
                }
            }

            SilicaListView {
                width: parent.width
                height: root.height - y
                model: listStocks
                delegate: DelegateStock {
                    onClicked: pageStack.push(Qt.resolvedUrl("StockDetailsPage.qml"), {
                        uid: model.uid,
                        ticker: model.ticker,
                        name: model.name
                    })
                }

                ViewPlaceholder {
                    enabled: !isLoading && !isError && listStocks.count === 0
                    text: qsTr("Your portfolio is empty")
                    hintText: qsTr("Add some stocks to see them here")
                }

                VerticalScrollDecorator {}
            }
        }
    }

    BusyIndicator {
        anchors.centerIn: parent
        size: BusyIndicatorSize.Large
        running: isLoading
        visible: isLoading
    }

    Label {
        anchors.centerIn: parent
        width: parent.width - Theme.horizontalPageMargin * 2
        text: qsTr("An error occurred while loading your portfolio")
        wrapMode: Text.Wrap
        horizontalAlignment: Text.AlignHCenter
        visible: isError
        color: Theme.highlightColor
    }

    Component.onCompleted: getPortfolio()
}
