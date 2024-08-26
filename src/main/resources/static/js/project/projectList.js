// projectList.js
new Vue({
    el: '[data-app="projectList"]',
    data: {
        tasks: [],
        projectMembers: [],
        mid: email,
        newTask: {
            title: '',
            dueDate: '',
            priority: ''
        },
        sortCriteria: '',
        filterCriteria: '',
        openSections: ['todo_section', 'doing_section', 'done_section', 'hold_section'],
        selectedTask: null,
        interval: null
    },
    created() {
        this.project = window.currentProject;
        this.getTasks();
        this.startInterval();
        this.getProjectMembers();
    },
    beforeDestroy() {
        this.stopInterval();
    },
    methods: {
        getTasks() {
            axios.get(`/tasks/project/${this.project.pid}`)
                .then(response => {
                    this.tasks = response.data;
                    this.updateTaskSections();
                })
                .catch(error => {
                    console.log(error);
                });
        },
        updateTaskSections() {
            this.filterAndSortTasks();
            const taskSections = ['todo', 'doing', 'done', 'hold'];
            taskSections.forEach(section => {
                const sectionTasks = this.filteredTasks.filter(task => task.taskStatus === section.toUpperCase());
                const sectionElement = document.getElementById(`${section}_section`);
                sectionElement.innerHTML = '';
                sectionTasks.forEach(task => {
                    const taskElement = this.createTaskElement(task);
                    sectionElement.appendChild(taskElement);
                });
            });
        },
        filterAndSortTasks() {
            let filteredTasks = [...this.tasks]; // 배열 복사
            if (this.filterCriteria === 'incomplete') {
                filteredTasks = filteredTasks.filter(task => task.taskStatus !== 'DONE');
            } else if (this.filterCriteria === 'complete') {
                filteredTasks = filteredTasks.filter(task => task.taskStatus === 'DONE');
            }
            if (this.sortCriteria === 'dueDate') {
                filteredTasks.sort((a, b) => new Date(a.dueDate) - new Date(b.dueDate));
            } else if (this.sortCriteria === 'priority') {
                const priorityOrder = ['HIGH', 'MEDIUM', 'LOW'];
                filteredTasks.sort((a, b) => priorityOrder.indexOf(a.priority) - priorityOrder.indexOf(b.priority));
            }
            this.filteredTasks = filteredTasks;
        },
        startInterval() {
            this.interval = setInterval(() => {
                this.updateTaskSections();
            }, 60000); // 1분마다 업데이트
        },
        stopInterval() {
            clearInterval(this.interval);
        },
        createTaskElement(task) {
            const taskElement = document.createElement('div');
            taskElement.classList.add('projectList_task_item');
            taskElement.addEventListener('click', () => this.openDetailPane(task));

            const dueDate = new Date(task.dueDate);
            const dueDateString = this.getFormattedDueDateString(dueDate);

            const priority = task.priority ? task.priority : '';

            taskElement.innerHTML = `
                <div class="task-details">
                    <span class="projectList_task_name">${task.title}</span>
                    <span class="avatar">${task.mid}</span>
                    <span class="calendar">${dueDateString}</span>
                    <span class="projectList_status_priority">${priority}</span>
                </div>
            `;
            return taskElement;
        },
        getFormattedDueDateString(dueDate) {
            const now = new Date();
            const diffInDays = Math.ceil((dueDate - now) / (1000 * 60 * 60 * 24));
            const options = { month: 'long', day: 'numeric' };

            if (diffInDays === 1) {
                return '내일';
            } else if (diffInDays < 7) {
                const dayName = dueDate.toLocaleDateString('ko-KR', { weekday: 'long' });
                return `${dayName}`;
            } else {
                const formattedDate = dueDate.toLocaleDateString('ko-KR', options);
                return formattedDate;
            }
        },
        openModal(modalId) {
            const modal = document.getElementById(modalId);
            modal.style.display = 'block';
        },
        closeModal(modalId) {
            const modal = document.getElementById(modalId);
            modal.style.display = 'none';
        },
        openDetailPane(task) {
            this.selectedTask = task;
            const detailPane = document.getElementById('detailPane');
            detailPane.classList.add('open');
        },
        closeDetailPane() {
            const detailPane = document.getElementById('detailPane');
            detailPane.classList.remove('open');
            this.selectedTask = null;
        },
        addTask() {
            const task = {
                ...this.newTask,
                mid: this.mid,
                pid: this.project.pid,
                taskStatus: 'TODO'
            };
            console.log("mid:", this.mid); // mid 값을 확인합니다.
            axios.post('/tasks', task)
                .then(response => {
                    this.tasks.push(response.data);
                    this.updateTaskSections();
                    this.newTask = {
                        title: '',
                        dueDate: '',
                        priority: '',
                    };
                    this.closeModal('addTaskModal');
                })
                .catch(error => {
                    console.log(error);
                });
        },
        saveChanges(field, event) {
            let newValue;
            switch (field) {
                case 'title':
                    newValue = event.target.innerText.trim();
                    if (newValue === '') {
                        this.deleteTask(this.selectedTask.tid);
                        this.closeDetailPane();
                        break;
                    }
                    // 다음 case로 이동
                case 'dueDate':
                case 'priority':
                case 'taskStatus':
                    newValue = event.target.value;
                    break;
                case 'content':
                     newValue = event.target.innerText.replace(/^내용: /, '').trim();
                    break;
            }
            const updatedTask = {
                ...this.selectedTask,
                [field]: newValue
            };
            this.updateTask(updatedTask);
        },
        updateTask(updatedTask) {
            axios.put(`/tasks/${updatedTask.tid}`, updatedTask)
                .then(response => {
                    const updatedIndex = this.tasks.findIndex(task => task.tid === updatedTask.tid);
                    Vue.set(this.tasks, updatedIndex, response.data);
                    this.updateTaskSections();  // 변경 사항 적용 후 UI 업데이트
                })
                .catch(error => {
                    console.log(error);
                });
        },
        deleteTask(tid) {
            axios.delete(`/tasks/${tid}`)
                .then(() => {
                    this.tasks = this.tasks.filter(task => task.tid !== tid);
                })
                .catch(error => {
                    console.log(error);
                });
        },
        sortTasks(criteria) {
            this.sortCriteria = criteria;
            this.updateTaskSections();
            this.closeModal('sortModal');
        },
        filterTasks(criteria) {
            this.filterCriteria = criteria;
            this.updateTaskSections();
            this.closeModal('filterModal');
        },
        toggleSection(sectionId) {
            const index = this.openSections.indexOf(sectionId);
            if (index > -1) {
                this.openSections.splice(index, 1);
            } else {
                this.openSections.push(sectionId);
            }
        },
        isSectionOpen(sectionId) {
            return this.openSections.includes(sectionId);
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
                mid: email
            };
            this.updateTask(updatedTask);
        }
    }
});
