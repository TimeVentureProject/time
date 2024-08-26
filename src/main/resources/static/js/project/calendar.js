new Vue({
    el: '#calendar',
    data: {
        tasks: [],
        calendar: null
    },
    created() {
        this.project = window.currentProject;
        this.fetchTasks();
    },
    mounted() {
        this.initializeCalendar();
    },
    methods: {
        fetchTasks() {
            axios.get(`/tasks/project/${this.project.pid}`)
                .then(response => {
                    this.tasks = response.data;
                    this.renderEvents();
                })
                .catch(error => {
                    console.error(error);
                });
        },
        initializeCalendar() {
            this.calendar = new FullCalendar.Calendar(document.getElementById('calendar'), {
                initialView: 'dayGridMonth',
                events: [],
                editable: true,
                eventDrop: this.handleEventDrop,
                eventClick: this.handleEventClick,
                eventDidMount: this.handleEventDidMount
            });
            this.calendar.render();
        },
        renderEvents() {
            const events = this.tasks.map(task => ({
                title: task.title,
                start: task.dueDate,
                extendedProps: {
                    taskId: task.tid
                },
                color: task.priority === 'HIGH' ? 'red' : task.priority === 'MEDIUM' ? 'orange' : 'green'
            }));
            this.calendar.removeAllEvents();
            this.calendar.addEventSource(events);
        },
        handleEventDrop(info) {
            const taskId = info.event.extendedProps.taskId;
            const newDueDate = info.event.start;
            const task = this.tasks.find(task => task.tid === taskId);
            if (task) {
                task.dueDate = newDueDate;
                axios.put(`/tasks/${taskId}`, task)
                    .then(response => {
                        console.log('Task due date updated:', response.data);
                    })
                    .catch(error => {
                        console.error('Failed to update task due date:', error);
                    });
            }
        },
        handleEventClick(info) {
            const taskId = info.event.extendedProps.taskId;
            // 작업 상세 정보 페이지로 이동하거나 모달창 열기
            // 예: window.location.href = `/tasks/${taskId}`;
            // 또는 this.openTaskDetails(taskId);
        },
        handleEventDidMount(info) {
            const event = info.event;
            const task = this.tasks.find(task => task.tid === event.extendedProps.taskId);
            if (task) {
                const startDate = new Date(task.createdDate);
                const endDate = new Date(task.dueDate);
                const eventElement = info.el;
                const eventBackgroundElement = document.createElement('div');
                eventBackgroundElement.className = 'event-background';
                eventBackgroundElement.style.position = 'absolute';
                eventBackgroundElement.style.left = '0';
                eventBackgroundElement.style.right = '0';
                eventBackgroundElement.style.top = '0';
                eventBackgroundElement.style.bottom = '0';
                eventBackgroundElement.style.zIndex = '-1';
                eventElement.appendChild(eventBackgroundElement);
            }
        }
    }
});