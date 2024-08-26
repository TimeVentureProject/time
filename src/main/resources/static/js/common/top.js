// top.js
new Vue({
    el: '#top',
    data: {
        searchWord: '',
        searchResults: [],
        showSearchResults: false,
        selectedProject: '',
        selectedMember: '',
        selectedStatus: '',
        projects: [],
        members: []
    },
    created() {
        window.addEventListener('myProjectsLoaded', () => {
            this.projects = window.myProjects;
        });
    },
    methods: {
        searchTasks() {
            if (this.searchWord.trim() !== '') {
                const params = {
                    searchWord: this.searchWord
                };

                if (this.selectedProject) {
                    params.pid = this.selectedProject;
                }

                if (this.selectedMember) {
                    params.mid = this.selectedMember;
                }

                if (this.selectedStatus) {
                    params.status = this.selectedStatus;
                }

                axios.get('/tasks/search', { params })
                    .then(response => {
                        this.searchResults = response.data;
                    })
                    .catch(error => {
                        console.error('검색 중 오류 발생:', error);
                    });
            } else {
                this.searchResults = [];
            }
        },
        hideSearchResults() {
            setTimeout(() => {
                this.showSearchResults = false;
            }, 10);
        },
        onClickOutside(event) {
            if (!this.$el.contains(event.target)) {
                this.showSearchResults = false;
            }
        },
        fetchMembers() {
            if (this.selectedProject) {
                axios.get(`/api/projectMember/${this.selectedProject}`)
                    .then(response => {
                        this.members = response.data;
                    })
                    .catch(error => {
                        console.error('멤버 목록을 가져오는 중 오류 발생:', error);
                    });
            } else {
                this.members = [];
            }
        }
    },
    watch: {
        selectedProject() {
            this.fetchMembers();
            this.searchTasks();
        }
    },
    mounted() {
        document.addEventListener('click', this.onClickOutside);
    },
    beforeDestroy() {
        document.removeEventListener('click', this.onClickOutside);
    }

});