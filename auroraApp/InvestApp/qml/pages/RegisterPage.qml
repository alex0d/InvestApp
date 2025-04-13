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
                title: qsTr("Register")
            }

            Item { width: parent.width; height: Theme.paddingLarge }

            TextField {
                id: firstnameField
                width: parent.width
                placeholderText: qsTr("First name")
                label: qsTr("First name")
                EnterKey.enabled: text.length > 0
                EnterKey.iconSource: "image://theme/icon-m-enter-next"
                EnterKey.onClicked: lastnameField.focus = true
            }

            TextField {
                id: lastnameField
                width: parent.width
                placeholderText: qsTr("Last name (optional)")
                label: qsTr("Last name")
                EnterKey.enabled: true
                EnterKey.iconSource: "image://theme/icon-m-enter-next"
                EnterKey.onClicked: emailField.focus = true
            }

            TextField {
                id: emailField
                width: parent.width
                placeholderText: qsTr("Email")
                label: qsTr("Email")
                inputMethodHints: Qt.ImhEmailCharactersOnly
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
                EnterKey.iconSource: "image://theme/icon-m-enter-next"
                EnterKey.onClicked: confirmPasswordField.focus = true
            }

            PasswordField {
                id: confirmPasswordField
                width: parent.width
                placeholderText: qsTr("Confirm password")
                label: qsTr("Confirm password")
                EnterKey.enabled: text.length > 0
                EnterKey.iconSource: "image://theme/icon-m-enter-accept"
                EnterKey.onClicked: register()
            }

            Item { width: parent.width; height: Theme.paddingLarge }

            Button {
                text: qsTr("Register")
                anchors.horizontalCenter: parent.horizontalCenter
                onClicked: register()
                enabled: !isLoading && firstnameField.text.length > 0 &&
                        emailField.text.length > 0 && passwordField.text.length > 0 &&
                        confirmPasswordField.text.length > 0
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

    function register() {
        if (firstnameField.text.length === 0 || emailField.text.length === 0 ||
            passwordField.text.length === 0 || confirmPasswordField.text.length === 0) {
            errorLabel.text = qsTr("Please fill in all required fields")
            errorLabel.visible = true
            return
        }

        if (passwordField.text !== confirmPasswordField.text) {
            errorLabel.text = qsTr("Passwords do not match")
            errorLabel.visible = true
            return
        }

        isLoading = true
        errorLabel.visible = false

        var lastname = lastnameField.text.length > 0 ? "'" + lastnameField.text + "'" : "null"

        libKMPShared.run(
            "composeApp.ru.alex0d.investapp.aurora.userRepository.register('" +
            firstnameField.text + "', " + lastname + ", '" +
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
                        case "EMAIL_ALREADY_REGISTERED":
                            errorMessage = qsTr("Email already registered")
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
