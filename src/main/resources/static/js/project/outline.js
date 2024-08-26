new Vue({
    el: '#app',
    data: {
        inviteEmail: '',
        members: [],
        projectData: null,
        modalVisible: false,
        projectId: '', // Default project ID
        managerMember: null, // 추가
        memberList: []
    },
    mounted() {
        this.project = window.currentProject;
        this.fetchMembers1();
        this.initializeModal();
        this.projectExplain();
        this.findManagerMember();
        this.populateMemberList1();
    },

    //멤버 리스트 출력, 카운트
    methods: {
        fetchMembers1() {
            axios.get(`/api/members/${this.project.pid}`)
                .then(response => {
                    this.members = response.data;
                    document.getElementById('member-count').innerText = this.members.length;
                    this.populateMemberList();
                    this.findManagerMember();
                    this.populateMemberList1();
                })
                .catch(error => console.error('Error fetching members:', error));
        },

        findManagerMember() {
                this.managerMember = this.members.find(member => member.auth === 'MANAGER');
                if (this.managerMember) {
                    this.setManagerMemberDetails();
                }
            },

        setManagerMemberDetails() {
                        const profileImageElement = document.querySelector('.outline-image-profile-1');
                        profileImageElement.src = "/upload/"+this.managerMember.img;

                        const nameElement = document.querySelector('.outline-text-name-1');
                        nameElement.textContent = this.managerMember.name;

                        const authElement = document.querySelector('.outline-text-master-1');
                        authElement.textContent = this.managerMember.auth;
                    },

            populateMemberList1() {
                const memberList = document.getElementById('member-list1');
                memberList.innerHTML = '';

                const members = this.members.filter(member => member.auth === 'MEMBER');
                const rows = Math.ceil(members.length / 4);

                for (let i = 0; i < rows; i++) {
                    const rowDiv = document.createElement('div');
                    rowDiv.className = 'member-row';
                    memberList.appendChild(rowDiv);

                    const start = i * 4;
                    const end = Math.min(start + 4, members.length);

                    for (let j = start; j < end; j++) {
                        const member = members[j];

                        const memberDiv = document.createElement('div');
                        memberDiv.className = 'outline-group-detail-2';

                        const profileImgDiv = document.createElement('img');
                        profileImgDiv.className = 'outline-image-profile-2';
                        profileImgDiv.src = "/upload/"+member.img;

                        const containerDiv = document.createElement('div');
                        containerDiv.className = 'container';

                        const nameDiv = document.createElement('span');
                        nameDiv.className = 'outline-text-name-2';
                        nameDiv.innerText = member.name;

                        const authDiv = document.createElement('span');
                        authDiv.className = 'outline-group-add-button-2';
                        authDiv.innerText = member.auth;

                        containerDiv.appendChild(nameDiv);
                        containerDiv.appendChild(authDiv);
                        memberDiv.appendChild(profileImgDiv);
                        memberDiv.appendChild(containerDiv);
                        rowDiv.appendChild(memberDiv);
                    }
                }
            },
        //프로젝트 설명 출력 및 입력
        projectExplain() {
                    const projectSpan = document.querySelector('.outline-text-project-main');
                    projectSpan.textContent = this.project.pexplain;
        },

        populateMemberList() {
            const memberList = document.getElementById('member-list');
            memberList.innerHTML = '';

            this.members.forEach(member => {
                const memberDiv = document.createElement('div');
                memberDiv.className = 'member-group-member';

                const profileImgDiv = document.createElement('img');
                profileImgDiv.className = 'profile-img';
                profileImgDiv.src = "/upload/"+member.img;

                const nameDiv = document.createElement('div');
                nameDiv.className = 'member-name';
                nameDiv.innerText = member.name;

                const emailDiv = document.createElement('div');
                emailDiv.className = 'member-email';
                emailDiv.innerText = member.email;

                memberDiv.appendChild(profileImgDiv);
                memberDiv.appendChild(nameDiv);
                memberDiv.appendChild(emailDiv);

                memberList.appendChild(memberDiv);
            });
        },
        sendInvitation() {
        this.inviteEmail = document.getElementById('invite-member-input').value;
            if (this.validateEmail(this.inviteEmail)) {
                const managerEmail = email; // Assuming email is defined globally or fetched from session
                const projectId = this.project.pid;
                const projectName = this.project.pname;

                axios.post("/api/invite", {
                    email: this.inviteEmail,
                    managerEmail: managerEmail,
                    projectId: projectId,
                    projectName: projectName
                })
                .then(() => {
                    alert('초대장을 ' + this.inviteEmail + '로 보냈습니다.');
                    this.inviteEmail = '';
                })
                .catch(() => {
                    alert('초대장 전송에 실패했습니다.');
                });
            } else {
                alert('유효하지 않은 이메일 주소입니다.');
            }
        },
        validateEmail(email) {
            const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return re.test(email);
        },
        initializeModal() {
            const modal = document.getElementById('myModal');
            const btn = document.getElementById('openModalBtn');
            const span = document.getElementsByClassName('close')[0];

            btn.onclick = () => {
                modal.style.display = 'block';
            };

            span.onclick = () => {
                modal.style.display = 'none';
            };

            window.onclick = (event) => {
                if (event.target == modal) {
                    modal.style.display = 'none';
                }
            };
        }
    }
});