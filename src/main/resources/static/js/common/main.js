// main.js
document.addEventListener('DOMContentLoaded', () => {
    const sideMenuItems = document.querySelectorAll('.side-menu-item, .section-content');
    const contentContainer = document.querySelector('.content-container');
    let projectData = null;

    window.loadContent = function(target, container, projectData = null) {
        axios.get(target)
            .then(response => {
                container.innerHTML = response.data;
                handlePageScripts(target);
                if (target.startsWith('/project')) {
                    initNavMenuItems(container);
                    window.currentProject = projectData; // 전역 변수에 프로젝트 데이터 저장
                    loadProjectData(projectData);
                }
            })
            .catch(error => {
                console.error('Error loading content:', error);
            });
    }

    function handlePageScripts(target) {
            const scriptMap = {
                '/home': 'js/project/home.js',
                '/list': 'js/project/projectList.js',
                '/kanban': 'js/project/kanbanBoardList.js',
                '/dash': 'js/project/dashBoard.js',
                '/outline': 'js/project/outline.js',
                '/myPage': 'js/member/myPage.js',
                '/chat': 'js/project/chatting.js',
                '/calendar': 'js/project/calendar.js'
            };

            const scriptPath = scriptMap[target];
            if (scriptPath) {
                loadScript(scriptPath);
            }
        }

    function loadScript(url) {
        const script = document.createElement('script');
        script.src = url;
        document.body.appendChild(script);
    }

    function initNavMenuItems(container) {
        const navMenuItems = container.querySelectorAll('.navi-menu-item');
        const naviContentContainer = container.querySelector('.navi-content-container');

        navMenuItems.forEach(navItem => {
            navItem.addEventListener('click', () => {
                navMenuItems.forEach(item => item.classList.remove('active'));
                navItem.classList.add('active');
                const navTarget = navItem.getAttribute('data-target');
                loadContent(navTarget, naviContentContainer);
            });
        });
    }

    sideMenuItems.forEach(item => {
        item.addEventListener('click', () => {
            sideMenuItems.forEach(item => item.classList.remove('active'));
            item.classList.add('active');
            const target = item.getAttribute('data-target');
            if (target.startsWith('/project')) {
                const project = JSON.parse(item.getAttribute('data-project'));
                loadContent(target, contentContainer, project);
            } else {
                loadContent(target, contentContainer);
            }
        });
    });
});

document.addEventListener('DOMContentLoaded', function() {
    const topMenu = document.querySelector('.top-menu');
    const side = document.querySelector('.side');
    const contentContainer = document.querySelector('.content-container');

    // 사이드바를 기본적으로 표시
    side.classList.add('show');
    adjustContentContainerWidth();

    topMenu.addEventListener('click', function() {
        side.classList.toggle('show');
        adjustContentContainerWidth();
    });

    function adjustContentContainerWidth() {
        if (side.classList.contains('show')) {
            contentContainer.style.marginLeft = '200px';
            contentContainer.style.width = 'calc(100% - 200px)';
        } else {
            contentContainer.style.marginLeft = '0';
            contentContainer.style.width = '100%';
        }
    }
});

document.addEventListener("DOMContentLoaded", function() {
    // "홈" 버튼 자동 클릭
    document.getElementById("homeButton").click();
});

function loadProjectData(projectData) {
    var pid = new URLSearchParams(window.location.search).get('pid');
    if (projectData) {
            const pid = projectData.pid;
            const pname = projectData.pname;
            // pid와 pname을 사용하여 프로젝트 데이터 처리
            console.log('프로젝트 ID:', pid);
            console.log('프로젝트 이름:', pname);
            // 예시: 프로젝트 제목 표시
            document.querySelector('.project-title').textContent = pname;
        }
    if (pid) {
        // pid를 사용하여 프로젝트 데이터 로드 및 처리
        axios.get(`/api/project/${pid}`)
            .then(response => {
                const project = response.data;
                // 프로젝트 데이터를 사용하여 필요한 처리 수행
                console.log('프로젝트 데이터:', project);
                // 예시: 프로젝트 제목 표시
                document.querySelector('.project-title').textContent = project.pname;
            })
            .catch(error => {
                console.error('프로젝트 데이터를 가져오는 중 오류 발생:', error);
                alert('프로젝트 데이터를 가져오는 데 실패했습니다.');
            });
    }
}