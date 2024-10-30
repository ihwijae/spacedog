package com.spacedog.file.service;


import com.spacedog.file.domain.FileData;
import com.spacedog.file.repository.FileRepository;
import com.spacedog.file.repository.FileRepositoryImpl;
import com.spacedog.mock.FakeFileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Transactional
class FileLocalServiceTest {


    private FileRepository fileRepository = new FakeFileRepository();
    private FileLocalService fileLocalService;




    @BeforeEach
    void init () {
        fileLocalService = new FileLocalService(fileRepository);
        ReflectionTestUtils.setField(fileLocalService, "fileDir", "/Users/ihwijae/projects/spedok/file/"); //@Value에 넣을수있음
    }

    @Test
    void 이미지를_저장할_전체경로를_가져온다 () {

        //given
        String fileName = "test.png";


        //when
        String fullPath = fileLocalService.getFullPath(fileName);

        //then
        Assertions.assertThat(fullPath).isEqualTo("/Users/ihwijae/projects/spedok/file/test.png");
    }

    @Test
    void 이미지를_업로드_한다 () {
        //given
        MultipartFile multipartFile = new MockMultipartFile("file", "test.png", "image/png", "test".getBytes());



        //when
        FileData fileData = fileLocalService.uploadFile(multipartFile);

        //then
        Assertions.assertThat(fileData).isNotNull();
        Assertions.assertThat(fileData.getId()).isEqualTo(1L);
        Assertions.assertThat(fileData.getUploadFilename()).isEqualTo("test.png");
    }

    @Test
    void N개의_이미지를_업로드한다 () {

        //given
        List<MultipartFile> multipartFiles = new ArrayList<>();
        MultipartFile multipartFile1 = new MockMultipartFile("file", "test1.png", "image/png", "test".getBytes());
        MultipartFile multipartFile2 = new MockMultipartFile("file", "test2.png", "image/png", "test".getBytes());
        multipartFiles.add(multipartFile1);
        multipartFiles.add(multipartFile2);

        //when
        List<FileData> fileData = fileLocalService.uploadFiles(multipartFiles);



        //then
        Assertions.assertThat(fileData).isNotNull();
        Assertions.assertThat(fileData.size()).isEqualTo(2);
        Assertions.assertThat(fileData.get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(fileData.get(1).getId()).isEqualTo(2L);
        Assertions.assertThat(fileData.get(0).getUploadFilename()).isEqualTo("test1.png");
        Assertions.assertThat(fileData.get(1).getUploadFilename()).isEqualTo("test2.png");



    }

}