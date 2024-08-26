create table if not exists category
(
    category_id bigint auto_increment
    primary key,
    created_at  datetime(6)  not null,
    updated_at  datetime(6)  not null,
    name        varchar(255) not null
    );

create table if not exists entrepreneur
(
    created_at      datetime(6)  not null,
    deleted_at      datetime(6)  null,
    entrepreneur_id bigint auto_increment
    primary key,
    updated_at      datetime(6)  not null,
    business_number varchar(255) not null,
    detail_address  varchar(255) null,
    email           varchar(255) not null,
    image           varchar(255) null,
    name            varchar(255) not null,
    password        varchar(255) not null,
    phone_number    varchar(255) not null,
    postal          varchar(255) null,
    street_address  varchar(255) null,
    constraint UKilhjgj5cxoxgsyquiipjyapx0
    unique (email)
    );

create table if not exists entrepreneur_alarm
(
    alarm_id        bigint auto_increment
    primary key,
    created_at      datetime(6)                                         not null,
    entrepreneur_id bigint                                              not null,
    updated_at      datetime(6)                                         not null,
    content         varchar(255)                                        not null,
    type            enum ('ORDER_RECEIVED', 'RIDER_DISPATCH_COMPLETED') not null,
    constraint FKlenxngwbi6pbk8ihd712c3ehs
    foreign key (entrepreneur_id) references entrepreneur (entrepreneur_id)
    );

create table if not exists major
(
    created_at datetime(6)  not null,
    major_id   bigint auto_increment
    primary key,
    updated_at datetime(6)  not null,
    name       varchar(255) not null
    );

create table if not exists school
(
    created_at datetime(6)  not null,
    school_id  bigint auto_increment
    primary key,
    updated_at datetime(6)  not null,
    campus     varchar(255) not null,
    name       varchar(255) not null
    );

create table if not exists store
(
    close_time          time(6)      not null,
    max_delivery_time   int          not null,
    min_delivery_time   int          not null,
    open_time           time(6)      not null,
    created_at          datetime(6)  not null,
    deleted_at          datetime(6)  null,
    delivery_price      bigint       not null,
    entrepreneur_id     bigint       not null,
    min_purchase_amount bigint       not null,
    store_id            bigint auto_increment
    primary key,
    updated_at          datetime(6)  not null,
    description         varchar(255) not null,
    detail_address      varchar(255) not null,
    name                varchar(255) not null,
    phone_number        varchar(255) not null,
    postal              varchar(255) not null,
    street_address      varchar(255) not null,
    constraint FKmse7rdg1i07ovj2rk2i1wa4ky
    foreign key (entrepreneur_id) references entrepreneur (entrepreneur_id)
    );

create table if not exists holiday
(
    created_at  datetime(6)                                                                         not null,
    holiday_id  bigint auto_increment
    primary key,
    store_id    bigint                                                                              not null,
    updated_at  datetime(6)                                                                         not null,
    day_of_week enum ('FRIDAY', 'MONDAY', 'SATURDAY', 'SUNDAY', 'THURSDAY', 'TUESDAY', 'WEDNESDAY') not null,
    constraint UKp6rns17sur2ovyyrgf37jiofp
    unique (store_id, day_of_week),
    constraint FKctnbttj6fpcmmwua9o7fst6t4
    foreign key (store_id) references store (store_id)
    );

create table if not exists menu
(
    created_at  datetime(6)  not null,
    deleted_at  datetime(6)  null,
    menu_id     bigint auto_increment
    primary key,
    price       bigint       not null,
    store_id    bigint       not null,
    updated_at  datetime(6)  not null,
    description varchar(255) not null,
    image       varchar(255) null,
    name        varchar(255) not null,
    constraint UKaq1dlnmp8ou9ge4xhe4516f0r
    unique (store_id, name, price),
    constraint FK4sgenfcmk1jajhgctnkpn5erg
    foreign key (store_id) references store (store_id)
    );

create table if not exists store_category
(
    category_id       bigint      not null,
    created_at        datetime(6) not null,
    store_category_id bigint auto_increment
    primary key,
    store_id          bigint      not null,
    updated_at        datetime(6) not null,
    constraint UK4yruyvgiqnxhbk2548q9mrxh7
    unique (store_id, category_id),
    constraint FK3xftrk8cr9na0kj9c21kirtwt
    foreign key (store_id) references store (store_id),
    constraint FKm2p2repecp4mx2i2ibmw75deb
    foreign key (category_id) references category (category_id)
    );

create table if not exists store_image
(
    is_representative bit          not null,
    sequence          int          not null,
    created_at        datetime(6)  not null,
    image_id          bigint auto_increment
    primary key,
    store_id          bigint       not null,
    updated_at        datetime(6)  not null,
    url               varchar(255) not null,
    constraint FK8i0t3yr73c9h244pyv5mg6m4u
    foreign key (store_id) references store (store_id)
    );

create table if not exists store_school
(
    created_at      datetime(6) not null,
    school_id       bigint      not null,
    store_id        bigint      not null,
    store_school_id bigint auto_increment
    primary key,
    updated_at      datetime(6) not null,
    constraint UKb8eto22yonat6gtoavtlvnwmm
    unique (store_id, school_id),
    constraint FKaxl8iibd5aq5t9q6b9dorlj1b
    foreign key (school_id) references school (school_id),
    constraint FKpcmhndict6hecrctpwd46illa
    foreign key (store_id) references store (store_id)
    );

create table if not exists users
(
    is_banned      bit                                                        not null,
    created_at     datetime(6)                                                not null,
    deleted_at     datetime(6)                                                null,
    major_id       bigint                                                     not null,
    point          bigint                                                     not null,
    school_id      bigint                                                     not null,
    updated_at     datetime(6)                                                not null,
    user_id        bigint auto_increment
    primary key,
    account_number varchar(255)                                               null,
    account_owner  varchar(255)                                               null,
    detail_address varchar(255)                                               null,
    email          varchar(255)                                               not null,
    image          varchar(255)                                               null,
    name           varchar(255)                                               not null,
    nickname       varchar(255)                                               not null,
    password       varchar(255)                                               not null,
    phone_number   varchar(255)                                               not null,
    postal         varchar(255)                                               null,
    street_address varchar(255)                                               null,
    bank           enum ('HANA', 'KAKAO', 'KOOKMIN', 'NH', 'SHINHAN', 'TOSS') null,
    constraint UK6dotkott2kjsp8vw4d0m25fb7
    unique (email),
    constraint FKemivb8l5w3ax4ggsxnbdb3r45
    foreign key (school_id) references school (school_id),
    constraint FKr9xy1qys645mebakvhfkls5l3
    foreign key (major_id) references major (major_id)
    );

create table if not exists inquiry
(
    created_at datetime(6)                   not null,
    inquiry_id bigint auto_increment
    primary key,
    updated_at datetime(6)                   not null,
    user_id    bigint                        not null,
    answer     varchar(255)                  null,
    content    varchar(255)                  not null,
    title      varchar(255)                  not null,
    status     enum ('COMPLETED', 'PENDING') not null,
    constraint FKray80kmwpjpjb91ime7ogijjr
    foreign key (user_id) references users (user_id)
    );

create table if not exists inquiry_image
(
    sequence   int          not null,
    created_at datetime(6)  not null,
    image_id   bigint auto_increment
    primary key,
    inquiry_id bigint       not null,
    updated_at datetime(6)  not null,
    url        varchar(255) not null,
    constraint FK23ss3xptk07hdng8df3xtnrv
    foreign key (inquiry_id) references inquiry (inquiry_id)
    );

create table if not exists meeting
(
    is_early_payment_available bit                                                                                                                                                                           not null,
    max_headcount              int                                                                                                                                                                           not null,
    min_headcount              int                                                                                                                                                                           not null,
    created_at                 datetime(6)                                                                                                                                                                   not null,
    deleted_at                 datetime(6)                                                                                                                                                                   null,
    delivered_at               datetime(6)                                                                                                                                                                   null,
    leader_id                  bigint                                                                                                                                                                        not null,
    meeting_id                 bigint auto_increment
    primary key,
    payment_available_dt       datetime(6)                                                                                                                                                                   not null,
    store_id                   bigint                                                                                                                                                                        not null,
    updated_at                 datetime(6)                                                                                                                                                                   not null,
    delivered_detail_address   varchar(255)                                                                                                                                                                  not null,
    delivered_postal           varchar(255)                                                                                                                                                                  not null,
    delivered_street_address   varchar(255)                                                                                                                                                                  not null,
    description                varchar(255)                                                                                                                                                                  null,
    met_detail_address         varchar(255)                                                                                                                                                                  not null,
    met_postal                 varchar(255)                                                                                                                                                                  not null,
    met_street_address         varchar(255)                                                                                                                                                                  not null,
    purchase_type              enum ('DELIVERY_TOGETHER', 'DINING_TOGETHER')                                                                                                                                 not null,
    status                     enum ('COOKING', 'COOKING_COMPLETED', 'DELIVERY_COMPLETED', 'GATHERING', 'IN_DELIVERY', 'MEETING_CANCELLED', 'MEETING_COMPLETED', 'PURCHASE_CANCELLED', 'PURCHASE_COMPLETED') not null,
    constraint FKohkwkhswwrxsp68pe5ew46yyo
    foreign key (leader_id) references users (user_id),
    constraint FKtpqy4vinbsjgk4dl2fhp5urw3
    foreign key (store_id) references store (store_id)
    );

create table if not exists chat_room
(
    chat_room_id bigint auto_increment
    primary key,
    created_at   datetime(6) not null,
    meeting_id   bigint      not null,
    updated_at   datetime(6) not null,
    constraint UK79phtmpe8rh07un3hjuu2wd60
    unique (meeting_id),
    constraint FKl3j7n1kentww63x9q2uk7vu4y
    foreign key (meeting_id) references meeting (meeting_id)
    );

create table if not exists chat
(
    chat_id      bigint auto_increment
    primary key,
    chat_room_id bigint                 not null,
    created_at   datetime(6)            not null,
    updated_at   datetime(6)            not null,
    content      varchar(255)           not null,
    type         enum ('CHAT', 'ENTER') not null,
    constraint FK44b6elhh512d2722l09i6qdku
    foreign key (chat_room_id) references chat_room (chat_room_id)
    );

create table if not exists evaluate
(
    created_at   datetime(6)                                                                                                                      not null,
    evaluate_id  bigint auto_increment
    primary key,
    evaluatee_id bigint                                                                                                                           not null,
    evaluator_id bigint                                                                                                                           not null,
    meeting_id   bigint                                                                                                                           not null,
    updated_at   datetime(6)                                                                                                                      not null,
    content      enum ('BAD_RESPONSE', 'BAD_TIMECHECK', 'BAD_TOGETHER', 'GOOD_COMMUNICATION', 'GOOD_RESPONSE', 'GOOD_TIMECHECK', 'GOOD_TOGETHER') not null,
    constraint UK28gcrhpd4yoltq9fihh6qople
    unique (meeting_id, evaluatee_id, evaluator_id, content),
    constraint FK76wm1285uoes3h2nw3dpxa7c6
    foreign key (meeting_id) references meeting (meeting_id)
    );

create table if not exists purchase
(
    created_at  datetime(6)                                                                                                         not null,
    meeting_id  bigint                                                                                                              not null,
    purchase_id bigint auto_increment
    primary key,
    updated_at  datetime(6)                                                                                                         not null,
    user_id     bigint                                                                                                              not null,
    status      enum ('CANCEL', 'PAYMENT_COMPLETED', 'PAYMENT_FAILED', 'PAYMENT_REQUESTED', 'PRE_PURCHASE', 'PROGRESS', 'RECEIVED') not null,
    constraint FKalwx6453sptht1s9tl6b9m4qj
    foreign key (meeting_id) references meeting (meeting_id),
    constraint FKoj7ky1v8cf4ibkk0s7alikp52
    foreign key (user_id) references users (user_id)
    );

create table if not exists individual_purchase
(
    quantity               int         not null,
    created_at             datetime(6) not null,
    individual_purchase_id bigint auto_increment
    primary key,
    menu_id                bigint      not null,
    purchase_id            bigint      not null,
    updated_at             datetime(6) not null,
    constraint UKij37xoov9i6kvyevhrd5yi8s7
    unique (menu_id),
    constraint FK4nwpopgs88prkdm5sup4wbkiq
    foreign key (menu_id) references menu (menu_id),
    constraint FKiuh2go3fhuhplyj2pg2yck3mj
    foreign key (purchase_id) references purchase (purchase_id)
    );

create table if not exists individual_purchase_payment
(
    quantity               int          not null,
    created_at             datetime(6)  not null,
    individual_purchase_id bigint       not null,
    menu_id                bigint       not null,
    menu_price             bigint       not null,
    snapshot_id            bigint auto_increment
    primary key,
    updated_at             datetime(6)  not null,
    image                  varchar(255) not null,
    menu_description       varchar(255) not null,
    menu_name              varchar(255) not null,
    constraint FK7cc7oqdibokjnqhejgnviakkf
    foreign key (individual_purchase_id) references individual_purchase (individual_purchase_id)
    );

create table if not exists payment
(
    pg             tinyint                                                                                                                                                                                                                              not null
    check (`pg` between 0 and 31),
    amount         bigint                                                                                                                                                                                                                               not null,
    created_at     datetime(6)                                                                                                                                                                                                                          not null,
    payment_id     bigint auto_increment
    primary key,
    purchase_id    bigint                                                                                                                                                                                                                               not null,
    updated_at     datetime(6)                                                                                                                                                                                                                          not null,
    portone_uid    varchar(255)                                                                                                                                                                                                                         not null,
    transaction_id varchar(255)                                                                                                                                                                                                                         not null,
    method         enum ('ALIPAY', 'APPLEPAY', 'BOOKNLIFE', 'CARD', 'CULTUREGIFT', 'KAKAOPAY', 'KPAY', 'LPAY', 'NAVERPAY', 'PAYCO', 'PAYPAL', 'PHONE', 'POINT', 'SAMSUNG', 'SMARTCULTURE', 'SSGPAY', 'TOSSPAY', 'TRANS', 'UNIONPAY', 'VBANK', 'WECHAT') not null,
    status         enum ('FAILED', 'PAID', 'READY')                                                                                                                                                                                                     not null,
    constraint UK6ohgqce5txqxe8l8wkkkgjlc0
    unique (purchase_id),
    constraint UKtacis04bqalsngo46yvxlo7yb
    unique (transaction_id),
    constraint FKmm2h9p8cpu4741lwxyn2fnpgg
    foreign key (purchase_id) references purchase (purchase_id)
    );

create table if not exists purchase_payment
(
    created_at                datetime(6) not null,
    delivery_fee              bigint      not null,
    delivery_price            bigint      not null,
    individual_purchase_price bigint      null,
    point                     bigint      not null,
    purchase_id               bigint      not null,
    purchase_payment_id       bigint auto_increment
    primary key,
    team_purchase_fee         bigint      null,
    team_purchase_price       bigint      null,
    updated_at                datetime(6) not null,
    constraint FKrrg7ebhwt2fl8trl4r0iow0aa
    foreign key (purchase_id) references purchase (purchase_id)
    );

create table if not exists point
(
    amount              bigint                 not null,
    created_at          datetime(6)            not null,
    point_id            bigint auto_increment
    primary key,
    purchase_payment_id bigint                 null,
    updated_at          datetime(6)            not null,
    user_id             bigint                 null,
    content             varchar(255)           not null,
    type                enum ('MINUS', 'PLUS') not null,
    constraint FK5x0rfwo6q3kubvion9ecld8ya
    foreign key (user_id) references users (user_id),
    constraint FK6hh411wfkiah75ykurvfyvowl
    foreign key (purchase_payment_id) references purchase_payment (purchase_payment_id)
    );

create table if not exists team_purchase
(
    quantity         int         not null,
    created_at       datetime(6) not null,
    meeting_id       bigint      not null,
    menu_id          bigint      not null,
    team_purchase_id bigint auto_increment
    primary key,
    updated_at       datetime(6) not null,
    constraint UK4omv85i8bxeyxh3yvo5pabcol
    unique (menu_id),
    constraint FK7t1hlf7oct9t9hyahwubithgx
    foreign key (meeting_id) references meeting (meeting_id),
    constraint FKi1mo44119w4auluur4dbyjio7
    foreign key (menu_id) references menu (menu_id)
    );

create table if not exists team_purchase_payment
(
    quantity         int          not null,
    created_at       datetime(6)  not null,
    menu_id          bigint       not null,
    menu_price       bigint       not null,
    snapshot_id      bigint auto_increment
    primary key,
    team_purchase_id bigint       not null,
    updated_at       datetime(6)  not null,
    image            varchar(255) not null,
    menu_description varchar(255) not null,
    menu_name        varchar(255) not null,
    constraint FKdhhhj96e9y5v2aymgrpjq5xd4
    foreign key (team_purchase_id) references team_purchase (team_purchase_id)
    );

create table if not exists user_alarm
(
    alarm_id   bigint auto_increment
    primary key,
    created_at datetime(6)                                                                                                                                                             not null,
    updated_at datetime(6)                                                                                                                                                             not null,
    user_id    bigint                                                                                                                                                                  null,
    content    varchar(255)                                                                                                                                                            not null,
    type       enum ('COOKING_COMPLETED', 'DELIVERY_COMPLETED', 'DELIVERY_STARTED', 'MEETING_CONDITION_MET', 'ORDER_APPROVED', 'ORDER_COMPLETED', 'ORDER_REJECTED', 'REVIEW_RECEIVED') not null,
    constraint FK56hrhm2hboyuciaw7bu6knwx3
    foreign key (user_id) references users (user_id)
    );

