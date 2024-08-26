new Vue({
    el: '.chat-body',
    data: {
        comments: [],
        newComment: '',
        project: [],
        mid: email
    },
    created() {
        this.project = window.currentProject;
        this.fetchComments();
    },
    methods: {
        fetchComments() {
            axios.get(`/api/reviews/` + this.project.pid)
                .then(response => {
                    this.comments = response.data;
                    this.comments.forEach(comment => {
                        comment.isUser = comment.mid.email === this.mid;
                    });
                })
                .catch(error => {
                    console.error('댓글을 가져오는 중 오류가 발생했습니다.', error);
                });
        },
        addComment() {
            if (this.newComment.trim() !== '') {
                const commentData = {
                    pid: this.project.pid,
                    mid: this.mid,
                    content: this.newComment
                };
                axios.post('/api/reviews', commentData)
                    .then(response => {
                        const newComment = response.data;
                        newComment.isUser = true;
                        this.comments.push(newComment);
                        this.newComment = '';
                    })
                    .catch(error => {
                        console.error('댓글 작성 중 오류가 발생했습니다.', error);
                    });
            }
        },
        deleteComment(reviewId) {
            axios.delete(`/api/reviews/${reviewId}`)
                .then(() => {
                    this.comments = this.comments.filter(comment => comment.reviewId !== reviewId);
                })
                .catch(error => {
                    console.error('댓글 삭제 중 오류가 발생했습니다.', error);
                });
        },
        formatDate(dateString) {
            const date = new Date(dateString);
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');
            return `${year}-${month}-${day} ${hours}:${minutes}`;
        }
    }
});