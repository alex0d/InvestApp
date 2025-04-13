import QtQuick 2.6
import Sailfish.Silica 1.0
import "../tabview" as Tabs

Tabs.TabItem {
    id: root

    property bool isLoading: false
    property bool isError: false

    ListModel {
        id: searchResults
    }

    SilicaFlickable {
        anchors.fill: parent
        contentHeight: column.height + Theme.paddingLarge

        Column {
            id: column
            width: parent.width
            spacing: Theme.paddingMedium

            PageHeader {
                title: qsTr("Search Stocks")
            }

            SearchField {
                id: searchField
                width: parent.width
                placeholderText: qsTr("Enter ticker symbol (e.g. AAPL)")
                EnterKey.enabled: text.length > 0
                EnterKey.iconSource: "image://theme/icon-m-enter-accept"
                EnterKey.onClicked: search()

                onTextChanged: {
                    // Reset when user clears the field
                    if (text.length === 0) {
                        searchResults.clear()
                    }
                }
            }

            Button {
                text: qsTr("Search")
                anchors.horizontalCenter: parent.horizontalCenter
                onClicked: search()
                enabled: !isLoading && searchField.text.length > 0
            }

            SilicaListView {
                id: resultsList
                width: parent.width
                height: root.height - y
                model: searchResults
                visible: searchResults.count > 0

                delegate: ListItem {
                    id: delegate
                    width: parent.width
                    contentHeight: Theme.itemSizeMedium

                    Rectangle {
                        width: Theme.iconSizeMedium
                        height: Theme.iconSizeMedium
                        radius: width / 2
                        color: model.backgroundColor || Theme.highlightBackgroundColor
                        anchors.verticalCenter: parent.verticalCenter
                        anchors.left: parent.left
                        anchors.leftMargin: Theme.horizontalPageMargin

                        Label {
                            text: model.ticker.charAt(0)
                            color: model.textColor || Theme.primaryColor
                            font.pixelSize: Theme.fontSizeLarge
                            anchors.centerIn: parent
                        }
                    }

                    Column {
                        anchors.left: parent.left
                        anchors.leftMargin: Theme.horizontalPageMargin + Theme.iconSizeMedium + Theme.paddingMedium
                        anchors.right: priceColumn.left
                        anchors.rightMargin: Theme.paddingMedium
                        anchors.verticalCenter: parent.verticalCenter

                        Label {
                            text: model.ticker
                            font.pixelSize: Theme.fontSizeMedium
                            color: Theme.highlightColor
                            truncationMode: TruncationMode.Fade
                            width: parent.width
                        }

                        Label {
                            text: model.name
                            font.pixelSize: Theme.fontSizeSmall
                            color: Theme.secondaryColor
                            truncationMode: TruncationMode.Fade
                            width: parent.width
                        }
                    }

                    Column {
                        id: priceColumn
                        anchors.right: parent.right
                        anchors.rightMargin: Theme.horizontalPageMargin
                        anchors.verticalCenter: parent.verticalCenter
                        width: Theme.itemSizeLarge

                        Label {
                            anchors.right: parent.right
                            text: model.lastPrice.toLocaleString(Qt.locale(), 'f', 2)
                            font.pixelSize: Theme.fontSizeMedium
                            color: Theme.highlightColor
                        }

                        Label {
                            anchors.right: parent.right
                            text: model.currency
                            font.pixelSize: Theme.fontSizeSmall
                            color: Theme.secondaryColor
                        }
                    }

                    onClicked: {
                        pageStack.push(Qt.resolvedUrl("StockDetailsPage.qml"), {
                            uid: model.uid,
                            ticker: model.ticker,
                            name: model.name
                        })
                    }
                }

                VerticalScrollDecorator {}
            }

            Label {
                anchors.horizontalCenter: parent.horizontalCenter
                text: qsTr("No results found")
                color: Theme.secondaryColor
                font.pixelSize: Theme.fontSizeMedium
                visible: !isLoading && !isError && searchResults.count === 0 && searchField.text.length > 0
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
        text: qsTr("An error occurred while searching")
        wrapMode: Text.Wrap
        horizontalAlignment: Text.AlignHCenter
        visible: isError
        color: Theme.errorColor
    }

    function search() {
        if (searchField.text.length === 0) return

        isLoading = true
        isError = false
        searchResults.clear()

        libKMPShared.run(
            "composeApp.ru.alex0d.investapp.aurora.stockRepository.getSharesByTicker('" + searchField.text + "')",
            function(response) {
                isLoading = false

                if (response.array_1.length > 0) {
                    for (var i = 0; i < response.array_1.length; i++) {
                        searchResults.append(response.array_1[i])
                    }
                }
            },
            function(error) {
                console.log(error)
                isLoading = false
                isError = true
            }
        )
    }
}
