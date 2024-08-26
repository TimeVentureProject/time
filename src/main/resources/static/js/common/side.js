// side.js
new Vue({
    el: '#project-list',
    data: {
        projects: [],
        mid: email
    },
    mounted() {
        this.fetchProjects();
    },
    methods: {
        fetchProjects() {
            axios.get('/api/project/member', { params: { mid: email } })
                .then(response => {
                    this.projects = response.data;
                    window.myProjects = response.data;
                    window.dispatchEvent(new Event('myProjectsLoaded'));
                })
                .catch(error => {
                    console.error('프로젝트 목록을 가져오는 중 오류 발생:', error);
                    if (error.response) {
                        console.log(error.response.data);
                        console.log(error.response.status);
                        console.log(error.response.headers);
                      } else if (error.request) {
                        console.log(error.request);
                      } else {
                        console.log('Error', error.message);
                      }
                    alert('프로젝트 목록을 가져오는 데 실패했습니다.');
                });
        },
        goToProject(project) {
            const contentContainer = document.querySelector('.content-container');
            window.loadContent(`/project/${project.pid}`, contentContainer, project);
        }
    }
});

