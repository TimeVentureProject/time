// home.js
new Vue({
    el: '.home-container',
    data: {
        userName: name,
        mid: email,
        showDropdown: false,
        showNewTaskRow: false,
        tasks: [],
        newTask: {
            title: '',
            pid: '',
            dueDate: ''
        },
        currentFilter: 'upcoming',
        showCreateTaskModal: false,
        showProjectModal: false,
        projects: [],
        selectedTask: null,
        showTaskDetails: false,
        editMode: false,
    },
    computed: {
        filteredTasks() {
            if (this.currentFilter === 'upcoming') {
                return this.tasks.filter(task => task.taskStatus !== 'DONE');
            } else if (this.currentFilter === 'overdue') {
                return this.tasks.filter(task => this.isOverdue(task.dueDate));
            } else if (this.currentFilter === 'completed') {
                return this.tasks.filter(task => task.taskStatus === 'DONE');
            }
            return this.tasks;
        }
    },
    methods: {
        fetchUserName() {
            userName = this.userName;
        },
        toggleDropdown() {
            this.showDropdown = !this.showDropdown;
        },
        addNewTask() {
            this.showNewTaskRow = true;
        },
        openCreateTaskModal() {
            this.showCreateTaskModal = true;
        },
        closeCreateTaskModal() {
            this.showCreateTaskModal = false;
        },
        saveNewTask() {
            if (this.newTask.title.trim() !== '') {
                const formData = {
                    pid: this.newTask.pid,
                    mid: this.mid,
                    pmember: null,
                    title: this.newTask.title,
                    content: null,
                    priority: null,
                    taskStatus: 'TODO',
                    dueDate: this.newTask.dueDate
                };
                axios.post('/tasks', formData, { headers: { 'Content-Type': 'application/json' } })
                    .then(response => {
                        console.log('작업이 성공적으로 생성되었습니다.', response.data);
                        this.fetchTasks();
                        // 입력 필드 초기화
                        this.resetNewTask();
                        this.closeCreateTaskModal();
                    })
                    .catch(error => {
                        console.error('작업 생성 중 오류 발생:', error);
                        alert('작업 생성에 실패했습니다.');
                    });
            } else {
                this.resetNewTask;
            }
        },
        openProjectModal() {
            this.showProjectModal = true;
            this.fetchProjects();
        },
        closeProjectModal() {
            this.showProjectModal = false;
        },
        fetchProjects() {
            axios.get('/api/project/member', { params: { mid: email } })
                .then(response => {
                    this.projects = response.data;
                })
                .catch(error => {
                    console.error('프로젝트 목록을 가져오는 중 오류 발생:', error);
                    alert('프로젝트 목록을 가져오는 데 실패했습니다.');
                });
        },
        selectProject(project) {
            this.newTask.pid = project.pid;
            this.newTask.pname = project.pname;
            this.closeProjectModal();
        },
        cancelNewTask() {
            this.resetNewTask();
        },
        resetNewTask() {
            this.showNewTaskRow = false;
            this.newTask = {
                title: '',
                pid: '',
                dueDate: ''
            };
        },
        fetchTasks() {
            console.log("mid:", this.mid); // mid 값을 확인합니다.
            const atIndex = this.mid.indexOf('@');
            const encodedMid = encodeURIComponent(this.mid.slice(0, atIndex)) + this.mid.slice(atIndex);
            axios.get('/tasks', { params: { memberId: encodedMid } })
                .then(response => {
                    this.tasks = response.data;
                })
                .catch(error => {
                    console.error('작업 목록을 가져오는 중 오류 발생:', error);
                    alert('작업 목록을 가져오는 데 실패했습니다.');
                });
        },
        filterTasks(filter) {
            this.currentFilter = filter;
        },
        isOverdue(dueDate) {
            if (!dueDate) {
                return false;
            }
            const currentDate = new Date();
            return new Date(dueDate) < currentDate;
        },
        fetchProjects() {
            axios.get('/api/project/member', { params: { mid: email } })
                .then(response => {
                    this.projects = response.data;
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
        getProjectNameById(pid) {
            const project = this.projects.find(p => p.pid === pid);
            return project ? project.pname : '';
        },
        openTaskDetails(task) {
            this.selectedTask = { ...task };
            this.showTaskDetails = true;
        },
        closeTaskDetails() {
            this.selectedTask = null;
            this.showTaskDetails = false;
            this.editMode = false;
        },
        updateTask() {
            if (this.selectedTask.title.trim() === '') {
                this.deleteTask(this.selectedTask.tid);
            } else {
                axios.put(`/tasks/${this.selectedTask.tid}`, this.selectedTask)
                    .then(response => {
                        const updatedTask = response.data;
                        const index = this.tasks.findIndex(task => task.tid === updatedTask.tid);
                        if (index !== -1) {
                            this.tasks.splice(index, 1, updatedTask);
                        }
                        this.closeTaskDetails();
                    })
                    .catch(error => {
                        console.error(error);
                    });
            }
        },
        deleteTask(taskId) {
            axios.delete(`/tasks/${taskId}`)
                .then(response => {
                    this.tasks = this.tasks.filter(task => task.tid !== taskId);
                    this.closeTaskDetails();
                })
                .catch(error => {
                    console.error(error);
                });
        }
    },
    mounted() {
        this.fetchUserName();
        this.fetchTasks();
        this.fetchProjects();

        // 마감일 입력을 위한 달력 초기화
        flatpickr("#dueDate", {
            dateFormat: "Y-m-d",
            onClose: function(selectedDates, dateStr, instance) {
                this.newTask.dueDate = dateStr;
            }.bind(this)
        });
    }
});