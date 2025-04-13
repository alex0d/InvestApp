import QtQuick 2.0
import Sailfish.Silica 1.0

Page {
    id: root

    property bool isLoading: false

    SilicaFlickable {
        anchors.fill: parent
        contentHeight: column.height + Theme.paddingLarge

        Column {
            id: column
            width: parent.width
            spacing: Theme.paddingMedium

            PageHeader {
                title: qsTr("Login")
            }

            Item { width: parent.width; height: Theme.paddingLarge }

            TextField {
                id: emailField
                width: parent.width
                placeholderText: qsTr("Email")
                inputMethodHints: Qt.ImhEmailCharactersOnly
                label: qsTr("Email")
                errorHighlight: false
                EnterKey.enabled: text.length > 0
                EnterKey.iconSource: "image://theme/icon-m-enter-next"
                EnterKey.onClicked: passwordField.focus = true
            }

            PasswordField {
                id: passwordField
                width: parent.width
                placeholderText: qsTr("Password")
                label: qsTr("Password")
                EnterKey.enabled: text.length > 0
                EnterKey.iconSource: "image://theme/icon-m-enter-accept"
                EnterKey.onClicked: login()
            }

            Item { width: parent.width; height: Theme.paddingLarge }

            Button {
                text: qsTr("Login")
                anchors.horizontalCenter: parent.horizontalCenter
                onClicked: login()
                enabled: !isLoading && emailField.text.length > 0 && passwordField.text.length > 0
            }

            Item { width: parent.width; height: Theme.paddingMedium }

            Button {
                text: qsTr("Register")
                anchors.horizontalCenter: parent.horizontalCenter
                onClicked: pageStack.push(Qt.resolvedUrl("RegisterPage.qml"))
            }

            Text {
                id: errorLabel
                width: parent.width - Theme.horizontalPageMargin * 2
                anchors.horizontalCenter: parent.horizontalCenter
                color: Theme.errorColor
                font.pixelSize: Theme.fontSizeSmall
                wrapMode: Text.Wrap
                horizontalAlignment: Text.AlignHCenter
                visible: false
            }
        }
    }

    BusyIndicator {
        anchors.centerIn: parent
        size: BusyIndicatorSize.Large
        running: isLoading
        visible: isLoading
    }

    function login() {
        if (emailField.text.length === 0 || passwordField.text.length === 0) {
            errorLabel.text = qsTr("Please fill in all fields")
            errorLabel.visible = true
            return
        }

        isLoading = true
        errorLabel.visible = false

        libKMPShared.run(
            "composeApp.ru.alex0d.investapp.aurora.userRepository.authenticate('" +
            emailField.text + "', '" + passwordField.text + "')",
            function(response) {
                isLoading = false
                if (response.name_1 === "SUCCESS") {
                    // Navigate to main page
                    pageStack.replaceAbove(null, Qt.resolvedUrl("MainPage.qml"))
                } else {
                    // Handle error
                    var errorMessage = ""
                    switch (response.name_1) {
                        case "USER_NOT_FOUND":
                            errorMessage = qsTr("User not found")
                            break
                        case "INVALID_CREDENTIALS":
                            errorMessage = qsTr("Invalid credentials")
                            break
                        default:
                            errorMessage = qsTr("Unknown error occurred")
                    }
                    errorLabel.text = errorMessage
                    errorLabel.visible = true
                }
            },
            function(error) {
                console.log(error)
                isLoading = false
                errorLabel.text = qsTr("Network error. Check your connection")
                errorLabel.visible = true
            }
        )
    }
}
