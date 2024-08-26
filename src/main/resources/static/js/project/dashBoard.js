new Vue({
    el: '#app',
    data: {
        tasks: [],
        uncompletedChartData: {
            labels: ['할 일', '진행 중', '완료', '보류'],
            datasets: [{
                data: [0, 0, 0, 0],
                backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0']
            }]
        },
        totalChartData: {
            labels: ['완료됨', '미완료'],
            datasets: [{
                data: [0, 0],
                backgroundColor: ['#FFCE56', '#36A2EB']
            }]
        }
    },
    computed: {
        completedTaskCount() {
            return this.tasks.filter(task => task.taskStatus === 'DONE').length;
        },
        uncompletedTaskCount() {
            return this.tasks.filter(task => task.taskStatus !== 'DONE').length;
        },
        overdueTaskCount() {
            const currentDate = new Date();
            return this.tasks.filter(task => task.taskStatus !== 'DONE' && new Date(task.dueDate) < currentDate).length;
        },
        totalTaskCount() {
            return this.tasks.length;
        }
    },
    created() {
        this.project = window.currentProject;
    },
    mounted() {
        this.fetchTasks();
    },
    methods: {
        fetchTasks() {
            axios.get(`/tasks/project/${this.project.pid}`)
                .then(response => {
                    this.tasks = response.data;
                    this.updateChartData();
                    this.renderCharts();
                })
                .catch(error => {
                    console.error(error);
                });
        },
        updateChartData() {
            const sectionCounts = {
                'TODO': 0,
                'DOING': 0,
                'DONE': 0,
                'HOLD': 0
            };
            this.tasks.forEach(task => {
                sectionCounts[task.taskStatus]++;
            });
            this.uncompletedChartData.datasets[0].data = [
                sectionCounts['TODO'],
                sectionCounts['DOING'],
                sectionCounts['DONE'],
                sectionCounts['HOLD']
            ];
            this.totalChartData.datasets[0].data = [
                this.completedTaskCount,
                this.uncompletedTaskCount
            ];
        },
        renderCharts() {
            const uncompletedCtx = this.$refs.uncompletedChart.getContext('2d');
            const totalCtx = this.$refs.totalChart.getContext('2d');
            new Chart(uncompletedCtx, {
                type: 'pie',
                data: this.uncompletedChartData,
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'top',
                        },
                        title: {
                            display: true,
                            text: '미완료 작업(섹션별)'
                        }
                    }
                }
            });
            new Chart(totalCtx, {
                type: 'doughnut',
                data: this.totalChartData,
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'top',
                        },
                        title: {
                            display: true,
                            text: '총 작업 수(완료 상태별)'
                        }
                    }
                }
            });
        }
    }
});