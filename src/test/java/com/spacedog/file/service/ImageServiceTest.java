package com.spacedog.file.service;


import com.spacedog.file.domain.ItemImage;
import com.spacedog.file.repository.FileRepository;
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

@Transactional
class ImageServiceTest {


    private FileRepository fileRepository = new FakeFileRepository();
    private ImageService imageService;




    @BeforeEach
    void init () {
//        itemImageService = new ItemImageService(fileRepository);
        ReflectionTestUtils.setField(imageService, "fileDir", "/Users/ihwijae/projects/spedok/file/"); //@Value에 넣을수있음
    }

    @Test
    void 이미지를_저장할_전체경로를_가져온다 () {

        //given
        String fileName = "test.png";


        //when
        String fullPath = imageService.getItemImageFullPath(fileName);

        //then
        Assertions.assertThat(fullPath).isEqualTo("/Users/ihwijae/projects/spedok/file/test.png");
    }

    @Test
    void 이미지를_업로드_한다 () {
        //given
        MultipartFile multipartFile = new MockMultipartFile("file", "test.png", "image/png", "test".getBytes());



        //when
        ItemImage itemImage = imageService.uploadItemFile(1L, multipartFile);

        //then
        Assertions.assertThat(itemImage).isNotNull();
        Assertions.assertThat(itemImage.getId()).isEqualTo(1L);
        Assertions.assertThat(itemImage.getUploadFileName()).isEqualTo("test.png");
    }

    @Test
    void N개의_이미지를_업로드한다 () {

        //given
        Long itemId = 1L;
        List<MultipartFile> multipartFiles = new ArrayList<>();
        MultipartFile multipartFile1 = new MockMultipartFile("file", "test1.png", "image/png", "test".getBytes());
        MultipartFile multipartFile2 = new MockMultipartFile("file", "test2.png", "image/png", "test".getBytes());
        multipartFiles.add(multipartFile1);
        multipartFiles.add(multipartFile2);

        //when
        List<ItemImage> fileData = imageService.uploadItemFiles(itemId, multipartFiles);



        //then
        Assertions.assertThat(fileData).isNotNull();
        Assertions.assertThat(fileData.size()).isEqualTo(2);
        Assertions.assertThat(fileData.get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(fileData.get(1).getId()).isEqualTo(2L);
        Assertions.assertThat(fileData.get(0).getUploadFileName()).isEqualTo("test1.png");
        Assertions.assertThat(fileData.get(1).getUploadFileName()).isEqualTo("test2.png");



    }

}