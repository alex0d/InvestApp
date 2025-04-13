import QtQuick 2.6
import Sailfish.Silica 1.0
import "../tabview" as Tabs

Page {
    id: root

    property bool footerPosition: true
    backNavigation: false

    Tabs.TabView {
        id: tabs

        property var _viewModel: [portfolioView, searchView, profileView]
        width: parent.width
        height: root.height

        header: footerPosition ? null : tabBar
        footer: footerPosition ? tabBar : null

        model: _viewModel

        Component {
            id: tabBar

            Tabs.TabBar {
                model: tabModel
            }
        }

        Component {
            id: portfolioView

            PortfolioPage {}
        }

        Component {
            id: searchView

            SearchStocksPage {}
        }

        Component {
            id: profileView

            ProfilePage {}
        }
    }

    ListModel {
        id: tabModel

        ListElement {
            title: qsTr("Portfolio")
            icon: "image://theme/icon-m-home"
            count: 0
        }
        ListElement {
            title: qsTr("Search")
            icon: "image://theme/icon-m-search"
            count: 0
        }
        ListElement {
            title: qsTr("Profile")
            icon: "image://theme/icon-m-person"
            count: 0
        }
    }

    Component.onCompleted: {
        checkAuthentication()
    }

    function checkAuthentication() {
        libKMPShared.run(
            "composeApp.ru.alex0d.investapp.aurora.userRepository.authenticateByTokensInDataBase()",
            function(response) {
                if (response.name_1 !== "SUCCESS") {
                    // Redirect to login page if not authenticated
                    pageStack.replaceAbove(null, Qt.resolvedUrl("LoginPage.qml"))
                }
            },
            function(error) {
                console.log(error)
                // Redirect to login page on error
                pageStack.replaceAbove(null, Qt.resolvedUrl("LoginPage.qml"))
            }
        )
    }
}
