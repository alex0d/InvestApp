import QtQuick 2.0
import Sailfish.Silica 1.0

ListItem {
    id: root
    width: parent.width
    contentHeight: Theme.itemSizeMedium

    Image {
        id: logo
        anchors.left: parent.left
        anchors.leftMargin: Theme.horizontalPageMargin
        anchors.verticalCenter: parent.verticalCenter
        width: Theme.iconSizeMedium
        height: Theme.iconSizeMedium
        source: "https://invest-brands.cdn-tinkoff.ru/" + model.logoUrl + "x160.png"
        fillMode: Image.PreserveAspectCrop

        Rectangle {
            anchors.fill: parent
            color: Theme.highlightBackgroundColor
            opacity: 0.1
            visible: logo.status !== Image.Ready
        }
    }

    Column {
        anchors.left: logo.right
        anchors.leftMargin: Theme.paddingMedium
        anchors.right: parent.right
        anchors.rightMargin: Theme.horizontalPageMargin
        anchors.verticalCenter: parent.verticalCenter

        Label {
            width: parent.width
            text: model.name
            font.pixelSize: Theme.fontSizeMedium
            truncationMode: TruncationMode.Fade
        }

        Row {
            width: parent.width
            spacing: Theme.paddingSmall

            Label {
                text: model.amount + " " + qsTr("pcs") + " · " + model.lastPrice.toLocaleString(Qt.locale(), 'f', 2) + " RUB"
                font.pixelSize: Theme.fontSizeExtraSmall
                color: Theme.secondaryColor
            }

            Label {
                text: model.profit.toLocaleString(Qt.locale(), 'f', 2) + " RUB (" + model.profitPercent.toLocaleString(Qt.locale(), 'f', 2) + "%)"
                font.pixelSize: Theme.fontSizeExtraSmall
                color: model.profit > 0 ? "green" : "red"
            }
        }
    }

    onClicked: {
        // Здесь должен быть код для перехода на экран деталей акции
    }
}
