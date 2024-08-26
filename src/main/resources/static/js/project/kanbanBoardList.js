// kanbanBoardList.js
new Vue({
    el: '[data-app="kanbanBoard"]',
    data: {
        mid: email,
        columns: [
            { id: 'todo', title: 'Todo' },
            { id: 'doing', title: 'Doing' },
            { id: 'done', title: 'Done' },
            { id: 'hold', title: 'Hold' }
        ],
        tasks: [],
        projectMembers: [],
        newTask: {
            title: '',
            content: '',
            priority: '',
            dueDate: '',
            pmember: null
        },
        showCreateForm: false,
        createFormStatus: null,
        showTaskDetails: false,
        selectedTask: null,
        editMode: false,
        sortKey: 'title'
    },
    created() {
        this.project = window.currentProject;
        this.fetchTasks();
        this.getProjectMembers();
    },
    methods: {
        fetchTasks() {
            axios.get(`/tasks/project/${this.project.pid}`)
                .then(response => {
                    this.tasks = response.data;
                })
                .catch(error => {
                    console.error(error);
                });
        },
        getTasksByStatus(status) {
            return this.tasks.filter(task => task.taskStatus === status.toUpperCase());
        },
        openCreateForm(status) {
            this.showCreateForm = true;
            this.createFormStatus = status;
        },
        createTask() {
            const task = {
                ...this.newTask,
                mid: this.mid,
                pid: this.project.pid,
                taskStatus: this.createFormStatus.toUpperCase(),
                pmember: this.getSelectedProjectMember()
            };
            if (!task.title || task.title.trim() === '') {
                return;
            }
            console.log(task);
            axios.post('/tasks', task)
                .then(response => {
                    this.tasks.push(response.data);
                    this.newTask = {
                        title: '',
                        content: '',
                        priority: '',
                        dueDate: '',
                        pmember: null
                    };
                    this.showCreateForm = false;
                    this.createFormStatus = null;
                })
                .catch(error => {
                    console.error(error);
                });
        },
        onDragStart(event, task) {
            event.dataTransfer.setData('text/plain', task.tid);
        },
        onDrop(event, status) {
            const taskId = event.dataTransfer.getData('text');
            const task = this.tasks.find(task => task.tid === parseInt(taskId));
            if (task && task.taskStatus !== status.toUpperCase()) {
                this.updateTaskStatus(taskId, status);
            }
        },
        updateTaskStatus(taskId, status) {
            const task = this.tasks.find(task => task.tid === parseInt(taskId));
            if (task) {
                task.taskStatus = status.toUpperCase();
                axios.put(`/tasks/${taskId}`, task)
                    .then(response => {
                        console.log(response.data);
                    })
                    .catch(error => {
                        console.error(error);
                    });
            }
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
            if (this.selectedTask) {
                const updatedTask = {
                    ...this.selectedTask,
                    pmember: this.getSelectedProjectMember()
                };
                axios.put(`/tasks/${this.selectedTask.tid}`, updatedTask)
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
        },
        getProjectMembers() {
            axios.get(`/api/projectMember/${this.project.pid}`)
                .then(response => {
                    this.projectMembers = response.data;
                })
                .catch(error => {
                    console.error(error);
                });
        },
        changeAssignee(email) {
            const updatedTask = {
                ...this.selectedTask,
                mid: email // memberEmail을 사용하여 담당자 저장
            };
            this.updateTask(updatedTask);
        },
        getAssigneeName(mid) {
            const member = this.projectMembers.find(member => member.memberId === mid);
            return member ? member.memberName : '';
        },
        formatDate(timestamp) {
            if (!timestamp) return '';
            const date = new Date(timestamp);
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            return `${year}-${month}-${day}`;
        },
        updateDueDate(event) {
            this.selectedTask.dueDate = new Date(event.target.value).toISOString();
        },
        getSelectedProjectMember() {
            if (this.selectedTask.pmember) {
                return {
                    projectMemberId: this.selectedTask.pmember.projectMemberId,
                    project: { pid: this.project.pid },
                    member: { email: this.selectedTask.pmember.memberEmail },
                    auth: this.selectedTask.pmember.auth
                };
            }
            return null;
        },
        updateTaskOrDelete() {
            if (this.selectedTask && this.selectedTask.title.trim() === '') {
                this.deleteTask(this.selectedTask.tid);
            } else if (this.selectedTask) {
                this.updateTask();
            }
        },
    },
    computed: {
        sortedTasks() {
            return this.tasks.sort((a, b) => {
                if (this.sortKey === 'title') {
                    return a.title.localeCompare(b.title);
                } else if (this.sortKey === 'priority') {
                    const priorities = ['LOW', 'MEDIUM', 'HIGH'];
                    return priorities.indexOf(a.priority) - priorities.indexOf(b.priority);
                } else if (this.sortKey === 'taskStatus') {
                    const statuses = ['TODO', 'DOING', 'DONE', 'HOLD'];
                    return statuses.indexOf(a.taskStatus) - statuses.indexOf(b.taskStatus);
                }
                return 0;
            });
        }
    }
});