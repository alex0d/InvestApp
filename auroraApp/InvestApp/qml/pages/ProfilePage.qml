import QtQuick 2.6
import Sailfish.Silica 1.0
import "../tabview" as Tabs

Tabs.TabItem {
    id: root

    property bool isLoading: true
    property string firstname: ""
    property string lastname: ""
    property string email: ""

    SilicaFlickable {
        anchors.fill: parent
        contentHeight: column.height + Theme.paddingLarge

        Column {
            id: column
            width: parent.width
            spacing: Theme.paddingMedium

            PageHeader {
                title: qsTr("Profile")
            }

            Item { width: parent.width; height: Theme.paddingLarge }

            Label {
                anchors.horizontalCenter: parent.horizontalCenter
                font.pixelSize: Theme.fontSizeExtraLarge
                color: Theme.highlightColor
                text: firstname + (lastname ? " " + lastname : "")
                visible: !isLoading
            }

            Label {
                anchors.horizontalCenter: parent.horizontalCenter
                font.pixelSize: Theme.fontSizeMedium
                color: Theme.secondaryHighlightColor
                text: email
                visible: !isLoading
            }

            Item { width: parent.width; height: Theme.paddingLarge * 2 }

            Button {
                text: qsTr("Logout")
                anchors.horizontalCenter: parent.horizontalCenter
                onClicked: logout()
                visible: !isLoading
            }
        }
    }

    BusyIndicator {
        anchors.centerIn: parent
        size: BusyIndicatorSize.Large
        running: isLoading
        visible: isLoading
    }

    function loadUserInfo() {
        isLoading = true

        // Load user firstname
        libKMPShared.run(
            "composeApp.ru.alex0d.investapp.aurora.userRepository.getUserFirstname()",
            function(response) {
                firstname = response || ""
                checkAllDataLoaded()
            },
            function(error) {
                console.log(error)
                firstname = ""
                checkAllDataLoaded()
            }
        )

        // Load user lastname
        libKMPShared.run(
            "composeApp.ru.alex0d.investapp.aurora.userRepository.getUserLastname()",
            function(response) {
                lastname = response || ""
                checkAllDataLoaded()
            },
            function(error) {
                console.log(error)
                lastname = ""
                checkAllDataLoaded()
            }
        )

        // Load user email
        libKMPShared.run(
            "composeApp.ru.alex0d.investapp.aurora.userRepository.getUserEmail()",
            function(response) {
                email = response || ""
                checkAllDataLoaded()
            },
            function(error) {
                console.log(error)
                email = ""
                checkAllDataLoaded()
            }
        )
    }

    // Helper to ensure all data is loaded
    property int dataLoadedCount: 0
    function checkAllDataLoaded() {
        dataLoadedCount++
        if (dataLoadedCount >= 3) {
            isLoading = false
            dataLoadedCount = 0
        }
    }

    function logout() {
        libKMPShared.run(
            "composeApp.ru.alex0d.investapp.aurora.userRepository.logout()",
            function() {
                // Redirect to login page
                pageStack.replaceAbove(null, Qt.resolvedUrl("LoginPage.qml"))
            },
            function(error) {
                console.log(error)
                // Still redirect to login page on error
                pageStack.replaceAbove(null, Qt.resolvedUrl("LoginPage.qml"))
            }
        )
    }

    Component.onCompleted: loadUserInfo()
}
